package com.elisbao.spring_nava_api.exceptions;

public record ValidationError(String field, String message) {
}