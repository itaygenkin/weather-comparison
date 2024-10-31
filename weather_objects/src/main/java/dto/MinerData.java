package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MinerData {
    private String url;
    private String minerName;

    // TODO: check if class is necessary
}
