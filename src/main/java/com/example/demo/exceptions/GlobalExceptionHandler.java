package com.example.demo.exceptions;

import com.example.demo.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {


        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ExceptionResponseDto> handleConflictRuntimeException(RuntimeException exception){

            ExceptionResponseDto responseDto = new ExceptionResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Something went wrong",
                    "An internal server issue occurred"
            );

            return ResponseEntity.ok().body(responseDto);
        }

        @ExceptionHandler(NoUserFoundByIdException.class)
        public ResponseEntity<ExceptionResponseDto> handleConflictNoUserFoundByIdException(NoUserFoundByIdException exception){

            ExceptionResponseDto responseDto = new ExceptionResponseDto(
                    exception.getHttpStatus().value(),
                    exception.getHttpStatus().getReasonPhrase(),
                    exception.getMessage()
            );

            return ResponseEntity.ok().body(responseDto);
        }

        @ExceptionHandler(NoAnuntFoundByIdException.class)
        public ResponseEntity<ExceptionResponseDto> handleConflictNoAnuntFoundByIdException(NoAnuntFoundByIdException exception){

            ExceptionResponseDto responseDto = new ExceptionResponseDto(
                    exception.getHttpStatus().value(),
                    exception.getHttpStatus().getReasonPhrase(),
                    exception.getMessage()
            );

            return ResponseEntity.ok().body(responseDto);
        }

        @ExceptionHandler(NoAnuntFoundByNameException.class)
        public ResponseEntity<ExceptionResponseDto> handleConflictNoAnuntFoundByNameException(NoAnuntFoundByNameException exception){

            ExceptionResponseDto responseDto = new ExceptionResponseDto(
                    exception.getHttpStatus().value(),
                    exception.getHttpStatus().getReasonPhrase(),
                    exception.getMessage()
            );

            return ResponseEntity.ok().body(responseDto);
        }

        @ExceptionHandler(NoUserFoundException.class)
        public ResponseEntity<ExceptionResponseDto> handleConflictNoUserFoundException(NoUserFoundException exception){

            ExceptionResponseDto responseDto = new ExceptionResponseDto(
                    exception.getHttpStatus().value(),
                    exception.getHttpStatus().getReasonPhrase(),
                    exception.getMessage()
            );

            return ResponseEntity.ok().body(responseDto);
        }

        @ExceptionHandler(NoAnuntFoundByUserIdException.class)
        public ResponseEntity<ExceptionResponseDto> handleConflictNoAnuntFoundByUserIdException(NoAnuntFoundByUserIdException exception){

            ExceptionResponseDto responseDto = new ExceptionResponseDto(
                    exception.getHttpStatus().value(),
                    exception.getHttpStatus().getReasonPhrase(),
                    exception.getMessage()
            );

            return ResponseEntity.ok().body(responseDto);
        }

    }
