
package com.example.latihansoal

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.latihansoal.ui.theme.LatihanSoalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LatihanSoalTheme {
                AplikasiPendaftaranSeminar()
            }
        }
    }
}

@Composable
fun AplikasiPendaftaranSeminar() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "form"
    ) {
        composable("form") {
            FormPendaftaran(
                onDaftar = { nama, nim, prodi, seminar ->
                    val route =
                        "detail/${Uri.encode(nama)}" +
                                "/${Uri.encode(nim)}" +
                                "/${Uri.encode(prodi)}" +
                                "/${Uri.encode(seminar)}"

                    navController.navigate(route)
                }
            )
        }

        composable(
            route = "detail/{nama}/{nim}/{prodi}/{seminar}",
            arguments = listOf(
                navArgument("nama") {
                    type = NavType.StringType
                },
                navArgument("nim") {
                    type = NavType.StringType
                },
                navArgument("prodi") {
                    type = NavType.StringType
                },
                navArgument("seminar") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            DetailPendaftaran(
                nama = entry.arguments?.getString("nama") ?: "",
                nim = entry.arguments?.getString("nim") ?: "",
                prodi = entry.arguments?.getString("prodi") ?: "",
                seminar = entry.arguments?.getString("seminar") ?: "",
                onKembali = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FormPendaftaran(
    onDaftar: (String, String, String, String) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var prodi by remember { mutableStateOf("") }

    var seminar by remember {
        mutableStateOf("Seminar Teknologi")
    }

    var dropdownExpanded by remember {
        mutableStateOf(false)
    }

    var pesanError by remember {
        mutableStateOf("")
    }

    val pilihanSeminar = listOf(
        "Seminar Teknologi",
        "Seminar Kewirausahaan"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Pendaftaran Seminar")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Formulir Pendaftaran Peserta",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Lengkap") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = nim,
                onValueChange = { nim = it },
                label = {
                    Text("Nomor Induk Mahasiswa (NIM)")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = prodi,
                onValueChange = { prodi = it },
                label = { Text("Program Studi") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Jenis Seminar",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Button(
                    onClick = {
                        dropdownExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(seminar)
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = {
                        dropdownExpanded = false
                    }
                ) {
                    pilihanSeminar.forEach { pilihan ->
                        DropdownMenuItem(
                            text = { Text(pilihan) },
                            onClick = {
                                seminar = pilihan
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (
                        nama.isBlank() ||
                        nim.isBlank() ||
                        prodi.isBlank()
                    ) {
                        pesanError = "Semua data wajib diisi!"
                    } else {
                        pesanError = ""

                        onDaftar(
                            nama.trim(),
                            nim.trim(),
                            prodi.trim(),
                            seminar
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Daftar Sekarang")
            }

            if (pesanError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = pesanError,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun DetailPendaftaran(
    nama: String,
    nim: String,
    prodi: String,
    seminar: String,
    onKembali: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detail Pendaftaran")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Pendaftaran Berhasil!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Nama Lengkap",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = nama)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nomor Induk Mahasiswa (NIM)",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = nim)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Program Studi",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = prodi)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Jenis Seminar",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = seminar)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onKembali,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali")
            }
        }
    }
}
