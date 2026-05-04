package pl.edu.gamestore.wrapper;

import java.time.OffsetDateTime;

public sealed interface ResponseError permits ValidationError, GeneralError {
    String message();

    OffsetDateTime timestamp();
}
