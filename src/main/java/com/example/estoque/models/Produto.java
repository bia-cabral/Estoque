package com.example.estoque.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Schema(description = "Representa um produto no sistema")
public class Produto {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Schema(description = "Representa o identificador do produto")
    private long id;
    @NotNull(message = "O nome não pode ser nulo! 😠")
    @Size(min = 2, message = "O nome deve ter no mínimo 2 caracteres! 😠")
    @Schema(description = "Representa o nome do produto no sistema")
    private String nome;
    @Schema(description = "Representa a descrição do produto no sistema")
    private String descricao;
    @NotNull(message = "O preço não pode ser nulo! 😠")
    @Min(value = 0, message = "O preço deve ser pelo menos 0! 😠")
    @Schema(description = "Representa o preço do produto no sistema")
    private double preco;
    @Column (name = "quantidadeestoque")
    @NotNull(message = "O estoque não pode ser nulo! 😠")
    @Min(value = 0, message = "O estoque deve ser pelo menos 0! 😠")
    @Schema(description = "Representa a quantidade de estoque de um produto no sistema")
    private int qntEstoque;

    public Produto() {
    }

    public Produto(long id, String nome, String descricao, double preco, int qntEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.qntEstoque = qntEstoque;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQntEstoque() {
        return qntEstoque;
    }

    public void setQntEstoque(int qntEstoque) {
        this.qntEstoque = qntEstoque;
    }

    @Override
    public String toString() {
        return "Id: " + this.id + "\n" +
                "Nome: " + this.nome + "\n" +
                "Descrição: " + this.descricao + "\n" +
                "Preço: " + this.preco + "\n" +
                "Quantidade: " + this.qntEstoque + "\n";
    }
}
