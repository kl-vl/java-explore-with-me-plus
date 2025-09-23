package ru.practicum.mainservice.compilation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import ru.practicum.mainservice.event.Event;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "compilation")
@Builder
@Entity
@Getter
@Setter
@NamedEntityGraph(name = "compilation-with-events", attributeNodes = @NamedAttributeNode("events"))
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "title")
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @BatchSize(size = 10)
    @JoinTable(
            name = "compilation_event",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private Set<Event> events;

    public void addEvent(Event event) {
        this.events.add(event);
        event.getCompilations().add(this);
    }

    public void removeEvent(Event event) {
        this.events.remove(event);
        event.getCompilations().remove(this);
    }
}
