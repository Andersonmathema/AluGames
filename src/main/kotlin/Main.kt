package org.Alura_alu_games

import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.util.Scanner


fun main() {
    // Importa a biblioteca Scanner para entrada de dados do usuário
    val leitura = Scanner(System.`in`)
    println("Digite um código de jogo para buscar:")

    // Lê o código de jogo fornecido pelo usuário
    val busca = leitura.nextLine()

    // Define o endpoint da API, incluindo o código do jogo na URL
    val endereco = "https://www.cheapshark.com/api/1.0/games?id=$busca"

    // Cria um cliente HTTP para fazer a requisição
    val client: HttpClient = HttpClient.newHttpClient()

    // Constrói a requisição HTTP GET para o endpoint especificado
    val request = HttpRequest.newBuilder()
        .uri(URI.create(endereco))
        .build()

    // Envia a requisição e armazena a resposta como string
    val response = client
        .send(request, BodyHandlers.ofString())

    // Armazena o corpo da resposta em formato JSON
    val json = response.body()
    println(json) // Exibe o JSON retornado pela API (útil para debug)

    // Inicializa o Gson para trabalhar com serialização e desserialização JSON
    val gson = Gson()

    // Declara a variável para armazenar as informações do jogo
    var meuInfoJogo: InfoJogo? = null

    // Tenta desserializar o JSON para um objeto da classe InfoJogo
    val resultadoIJ = runCatching {
        meuInfoJogo = gson.fromJson(
            json,
            InfoJogo::class.java // Classe que representa o JSON recebido
        )
    }

    // Lida com falhas na desserialização do JSON
    resultadoIJ.onFailure {
        println("Id informado inexistente. Tente outro id.")
        System.exit(1) // Finaliza o programa em caso de erro
    }

    // Declara a variável para armazenar o jogo
    var meuJogo: Jogo? = null

    // Tenta criar um objeto Jogo a partir das informações obtidas
    val resultado = runCatching {
        meuJogo = Jogo(
            meuInfoJogo!!.info.title, // Título do jogo
            meuInfoJogo!!.info.thumb  // Thumbnail (imagem) do jogo
        )
    }

    // Lida com erros na criação do objeto Jogo
    resultado.onFailure {
        println("Jogo inexistente, tente outro id.")
    }

    // Lida com sucesso na criação do objeto Jogo
    resultado.onSuccess {
        println("Deseja inserir uma descrição personalizada? S/N")

        // Pergunta ao usuário se ele quer adicionar uma descrição personalizada
        val opcao = leitura.nextLine()
        if (opcao.equals("s", ignoreCase = true)) {
            println("Insira a descrição personalizada para o jogo: ")
            // Lê a descrição personalizada e a atribui ao jogo
            val descricaoPersonalizada = leitura.nextLine()
            meuJogo?.descricao = descricaoPersonalizada
        } else {
            // Caso o usuário escolha não adicionar, usa o título como descrição
            meuJogo?.descricao = meuJogo?.titulo
        }

        // Exibe as informações do jogo
        println(meuJogo)
    }
}
