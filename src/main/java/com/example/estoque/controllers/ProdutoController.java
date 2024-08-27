package com.example.estoque.controllers;

import com.example.estoque.models.Produto;
import com.example.estoque.repository.ProdutoRepository;
import com.example.estoque.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private ProdutoService produtoService;

    @Autowired
    public void setProdutoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/selecionar")
    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista com todos os produtos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @Schema(description = "Lista todos os produtos")
    public List<Produto> listarProdutos(){
        return produtoService.buscarTodosProdutos();
    }

    @GetMapping("/selecionarID/{id}")
    @Operation(summary = "Busca um produto específico", description = "Retorna um único produto de acordo com o ID recebido como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @Schema(description = "Lista produtos por ID")
    public Produto selecionarPorId(@PathVariable Long id){
        return produtoService.buscarProdutoPorID(id);
    }

    @PostMapping("/inserir")
    @Operation(summary = "Faz a inserção de um produto", description = "Recebe os valores dos campos e insere um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto inserido com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @Schema(description = "Inserir produto")
    public ResponseEntity<?> inserirProduto(@Valid @RequestBody Produto produto, BindingResult result){

        if (result.hasErrors()){
            Map<String, String> erros = new HashMap<>();
            for (FieldError erro : result.getFieldErrors()) {
                // Coloque o nome do campo e a mensagem de erro no mapa
                erros.put(erro.getField(), erro.getDefaultMessage());
            }

            return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
        }

        return produtoService.salvarProduto(produto);
    }

    @DeleteMapping("/excluir/{id}")
    @Transactional
    @Operation(summary = "Exclui um produto específico", description = "Exclui um único produto de acordo com o ID recebido como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto removido com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @Schema(description = "Exclui produto por ID")
//    public ResponseEntity<String> excluirProduto(@PathVariable Long id){
//        Long qntDeletados = produtoRepository.deleteProdutoById(id);
//        if (qntDeletados > 0){
//            return ResponseEntity.ok("Produto excluído com sucesso! 😄");
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não excluido! ☹️");
//    }

    public ResponseEntity<String> excluirProduto(@PathVariable Long id){
        return produtoService.excluirProduto(id);
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualiza um produto específico", description = "Atualiza um único produto de acordo com o ID recebido como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @Schema(description = "Atualiza um produto")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @Valid @RequestBody Produto produtoAtualizado, BindingResult result){
        if (result.hasErrors()){
            Map<String, String> erros = new HashMap<>();
            for (FieldError erro : result.getFieldErrors()) {
                // Coloque o nome do campo e a mensagem de erro no mapa
                erros.put(erro.getField(), erro.getDefaultMessage());
            }

            return new ResponseEntity<>(erros, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return produtoService.atualizarProduto(id, produtoAtualizado);
    }

    @PatchMapping("/atualizarParcial/{id}")
    @Operation(summary = "Atualiza alguns campos de um produto específico", description = "Atualiza um único produto de acordo com o ID e os campos recebidos como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @Schema(description = "Atualiza parcialmente um produto")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        return produtoService.atualizarParcial(id, updates);
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Busca produtos baseado no nome", description = "Retorna uma lista de produtos de acordo com o valor recebido como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @Schema(description = "Busca produto por nome")
    public ResponseEntity<?> buscarPorNomeIgnoreCase(@RequestParam String nome){
        List<Produto> produtos = produtoService.procurarProdutosIgnoreCase(nome);
        if (produtos.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(produtos);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produtos não encontrados");
    }

    @DeleteMapping("/deletarPorQnt/{qnt}")
    @Transactional
    @Operation(summary = "Deleta uma lista de produtos", description = "Deleta uma lista de produtos que tem uma determinada quantidade de produtos no estoque de acordo com o parâmetro recebido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos atualizados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @Schema(description = "Deleta produto por quantidade")
    public ResponseEntity<String> deletarPorQnt(@PathVariable int qnt){
        int qntExluidos = produtoService.excluirPorQnt(qnt);
        if (qntExluidos > 0){
            return ResponseEntity.ok("Foram exluidos " + qntExluidos + " produtos!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontrei nenhum produto com estoque " + qnt);
    }

    @PatchMapping("/contarPorQnt/{qnt}")
    @Operation(summary = "Retorna a quantidade de produtos com um estoque", description = "Retorna quantos produtos que tem uma determinada quantidade de produtos no estoque de acordo com o parâmetro recebido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos atualizados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @Schema(description = "Contar produto por quantidade")
    public int contarPorQnt(@PathVariable int qnt){
        return produtoService.contarPorEstoque(qnt);
    }
}
