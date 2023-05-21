package com.example.demo.entity;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDto {
    private Long  id;

    @NotBlank
    @Column(nullable = false)
    private String titulo;
    private String descripcion;
    private String estado;

    @NotBlank
    @Column(nullable = false)
    private String usuario;

    public TaskDto(String titulo, String descripcion, String completada, String usuario) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = completada;
        this.usuario = usuario;
    }
}
