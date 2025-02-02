package com.enerjai.model;

public class Alimento {
    private int codigo;
    private String nome;
    private int energia_kcal;
    private float lipidos_g;
    private float hidratos_carbono_g;
    private float fibra_g;
    private float proteina_g;

    // Construtor vazio para o Firestore
    public Alimento() {}

    // Construtor com todos os campos
    public Alimento(int codigo, String nome, int energia_kcal, float lipidos_g,
                    float hidratos_carbono_g, float fibra_g, float proteina_g) {
        this.codigo = codigo;
        this.nome = nome;
        this.energia_kcal = energia_kcal;
        this.lipidos_g = lipidos_g;
        this.hidratos_carbono_g = hidratos_carbono_g;
        this.fibra_g = fibra_g;
        this.proteina_g = proteina_g;
    }

    // Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEnergia_kcal() {
        return energia_kcal;
    }

    public void setEnergia_kcal(int energia_kcal) {
        this.energia_kcal = energia_kcal;
    }

    public float getLipidos_g() {
        return lipidos_g;
    }

    public void setLipidos_g(float lipidos_g) {
        this.lipidos_g = lipidos_g;
    }

    public float getHidratos_carbono_g() {
        return hidratos_carbono_g;
    }

    public void setHidratos_carbono_g(float hidratos_carbono_g) {
        this.hidratos_carbono_g = hidratos_carbono_g;
    }

    public float getFibra_g() {
        return fibra_g;
    }

    public void setFibra_g(float fibra_g) {
        this.fibra_g = fibra_g;
    }

    public float getProteina_g() {
        return proteina_g;
    }

    public void setProteina_g(float proteina_g) {
        this.proteina_g = proteina_g;
    }
}
