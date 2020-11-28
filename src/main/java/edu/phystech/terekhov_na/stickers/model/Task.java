package edu.phystech.terekhov_na.stickers.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @JsonRawValue
    private String items = "[]";
    private String deadline;
    private Double workHours;
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tab_id")
    @JsonIgnore
    private Tab tab;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", items='" + items + '\'' +
                ", deadline=" + deadline +
                ", workHours=" + workHours +
                ", notes='" + notes + '\'' +
                ", have tab?='" + (tab != null) + '\'' +
                '}';
    }
}
