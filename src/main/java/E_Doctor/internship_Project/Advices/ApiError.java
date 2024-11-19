package E_Doctor.internship_Project.Advices;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String>subErrors;
}
