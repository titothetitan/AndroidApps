package co.titoschmidt.tutorial.jokerappdev.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import titoschmidt.tutorial.jokerappdev.R


class MainActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    installSplashScreen()

    setContentView(R.layout.activity_main)

    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)

    val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

    val navView = findViewById<NavigationView>(R.id.nav_view)

    val navController = findNavController(R.id.nav_host_fragment_content_main)
    // Associa o conteúdo do fragment com o respectivo título da barra
    appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.nav_home,
        R.id.nav_joke_day,
        R.id.nav_about
      ), drawerLayout
    )
    // Sincroniza as telas e menus com o Controlador
    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)

    }
  // Associa o evento do click
  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(this@MainActivity,R.id.nav_host_fragment_content_main)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}
