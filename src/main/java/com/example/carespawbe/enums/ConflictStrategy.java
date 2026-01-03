package com.example.carespawbe.enums;

public enum ConflictStrategy {
    ASK,        // preview only (dry-run)
    SKIP,       // skip conflicting occurrences
    OVERWRITE   // delete overwritable conflicts then create
}

