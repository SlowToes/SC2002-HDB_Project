package enums;

public enum SortBy {
    ALPHA_ASCENDING,        // A → Z
    ALPHA_DESCENDING,       // Z → A
    DATE_OPENING_EARLIEST,  // Earliest applicationOpeningDate
    DATE_OPENING_LATEST,    // Latest applicationOpeningDate
    DATE_CLOSING_EARLIEST,  // Earliest applicationClosingDate
    DATE_CLOSING_LATEST,    // Latest applicationClosingDate
    FLAT_TYPE_ASCENDING,    // e.g., 2-room, 3-room...
    FLAT_TYPE_DESCENDING,   // e.g., 3-room, 2-room...
    NEIGHBOURHOOD_ASCENDING, // e.g., Jurong East, Bukit Batok...
    NEIGHBOURHOOD_DESCENDING // e.g., Bukit Batok, Jurong East...
}
