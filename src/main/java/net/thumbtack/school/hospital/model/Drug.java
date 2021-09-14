package net.thumbtack.school.hospital.model;
import java.io.Serializable;
import java.util.Objects;
public class Drug implements Serializable {
    private String name;

    public Drug(String nameDrug) {
        this.name = nameDrug;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drug drug = (Drug) o;
        return Objects.equals(name, drug.name);
    }
}
