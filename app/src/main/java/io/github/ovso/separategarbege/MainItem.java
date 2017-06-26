package io.github.ovso.separategarbege;

import lombok.Data;
import lombok.ToString;

/**
 * Created by jaeho on 2017. 6. 25
 */
@Data @ToString public class MainItem {
  private String url;
  private String subject;
  private int position = -1;
}
