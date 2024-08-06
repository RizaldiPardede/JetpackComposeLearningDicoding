package com.example.submissionjetpackcompose

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.dicoding.jetheroes.ui.theme.JetPokemonsTheme
import com.example.submissionjetpackcompose.data.PokemonRepository
import com.example.submissionjetpackcompose.model.Pokemon
import com.example.submissionjetpackcompose.model.PokemonsData
import com.example.submissionjetpackcompose.navigation.NavigationItem
import com.example.submissionjetpackcompose.navigation.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonsApp(
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
    viewModel: PokemonsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = ViewModelFactory(PokemonRepository()))
) {
    val groupedPokemons by viewModel.groupedPokemons.collectAsState()
    val query by viewModel.query


    Box(modifier = modifier) {
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,

        ){
            item{
                SearchingBar(query = query, QueryChange = viewModel::search,
                modifier = Modifier.background(MaterialTheme.colors.primary))
            }
            groupedPokemons.forEach{ (initial, pokemons)->

                items(pokemons, key={it.id}){
                            PokemonListItem(
                                name = it.name,
                                photoUrl = it.photoURL,
                                modifier = Modifier.clickable { navigateToDetail(it.id) }
                            )
                }
            }



        }

    }



}

@Composable
fun navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailPokemon.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                PokemonsApp(navigateToDetail = {
                    navController.navigate(Screen.DetailPokemon.createRoute(it))
                })
            }

            composable(Screen.Profile.route) {
                Profile()
            }

            composable(
                route = Screen.DetailPokemon.route,
                arguments = listOf(navArgument("pokemonId") {
                    type = NavType.StringType

                }),
            ) {
                val id = it.arguments?.getString("pokemonId") ?: "1"
                Detail(id = id)
            }
        }


    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            ),
        )
        BottomNavigation {
            navigationItems.map { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun SearchingBar(
    query :String,
    QueryChange:(String)->Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = QueryChange,
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null)},
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = { Text(stringResource(R.string.findpokemon)) },
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
fun Profile(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,




    ) {
        Image(painter = painterResource(R.drawable.fotoku) ,
            contentDescription ="my Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape))

        Text(
            text = "Rizaldi Pardede",
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.ExtraBold
            )
            ,

        )

        Text(
            text = "rizaldipardede@student.ub.ac.id",
            fontWeight = FontWeight.Medium,

        )
    }

}

@Composable
fun Detail(
    id:String,
    viewModel: DetailViewModel = viewModel(
        factory = DetailViewModel.ViewModelDetailFactory(
            Injection.provideRepository()
        )
    ),


) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getPokemonById(id)
            }
            is UiState.Success -> {
                val data = uiState.data
                DetailContent(
                    data.name,
                    data.photoURL,
                    data.Type,
                    data.desc,
                    data.kelemahan

                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun DetailContent(name: String,photoUrl: String,Type:String,desc:String,kelemahan:String,
                  modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            AsyncImage(model = photoUrl, contentDescription =null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(8.dp)
                    .size(200.dp)
                    .clip(CircleShape)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.ExtraBold
                )
                ,
            )
            Text(
                text = "Type",
                fontWeight = FontWeight.Medium,

                )

            Text(
                text = Type,
                fontWeight = FontWeight.Medium,
                color = if (Type == "Fire"){Color.Red} else if (Type == "Electric"){Color.Yellow}
                else if (Type == "Grass"){Color.Green}else if (Type == "Water"){Color.Blue}else if (Type == "Fairy"){Color.Cyan} else{
                    Color.Black
                }

            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = desc,
                fontWeight = FontWeight.Medium,

                )


        }
        Column(horizontalAlignment = Alignment.Start,

            ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Kelemahan",
                fontWeight = FontWeight.Medium,

                )
            Text(
                text = kelemahan,
                fontWeight = FontWeight.Medium,
                color = if (kelemahan == "Fire"){Color.Red} else if (kelemahan == "Electric"){Color.Yellow}
                else if (kelemahan == "Grass"){Color.Green}else if (kelemahan == "Water"){Color.Blue}else if (kelemahan == "Fairy"){Color.Cyan} else{
                    Color.Black
                }

            )

        }

    }



}

@Preview(showBackground = true)
@Composable
fun PokemonsAppPreview() {
    JetPokemonsTheme {

    }



}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    JetPokemonsTheme {
            Detail(id = "2")
    }



}


@Composable
fun PokemonListItem(
    name: String,
    photoUrl: String,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AsyncImage(model = photoUrl, contentDescription =null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
                .clip(CircleShape)
        )
        Text(
            text = name,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 16.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PokemonsListItemPreview() {
    JetPokemonsTheme {
        PokemonListItem(
            "Pikachu",
            "https://static.wikia.nocookie.net/pokemon/images/0/0d/025Pikachu.png/revision/latest?cb=20200620223403",
        )
    }



}

@Preview(showBackground = true)
@Composable
fun NavigationPreview(){
    JetPokemonsTheme {
        navigation()
    }



}