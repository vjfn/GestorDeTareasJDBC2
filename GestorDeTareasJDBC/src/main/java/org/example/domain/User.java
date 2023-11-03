package org.example.domain;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @NonNull Long id;
    @NonNull  private String name;
    @NonNull private String email;
    private ArrayList<Tarea> tareas = new ArrayList<>(0);
}
