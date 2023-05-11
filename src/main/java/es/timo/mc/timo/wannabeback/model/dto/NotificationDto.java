package es.timo.mc.timo.wannabeback.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * The type Notification dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {


    /**
     * The Id.
     */
    private Long id;

    /**
     * The Title.
     */
    private String title;

    /**
     * The Body.
     */
    private String body;

    /**
     * The Created date.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
    private Date createdDate;


}
