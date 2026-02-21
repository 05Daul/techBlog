package daulspring.blogservice.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Table(name = "post_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostTagEntity {

  @EmbeddedId
  @AttributeOverrides({
      @AttributeOverride(name = "postId", column = @Column(name = "post_id", nullable = false)),
      @AttributeOverride(name = "tagId", column = @Column(name = "tag_id", nullable = false))
  })
  private PostTagId postTagId;


  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("postId")
  @JoinColumn(name = "post_id")
  private PostEntity post;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("tagId")
  @JoinColumn(name = "tag_id")
  private TagEntity tag;
}
