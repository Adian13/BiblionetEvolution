package it.unisa.c07.biblionet.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Copertina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] dati;

    private String key;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
