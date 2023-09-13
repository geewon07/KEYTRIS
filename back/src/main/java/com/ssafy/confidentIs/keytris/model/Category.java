package com.ssafy.confidentIs.keytris.model;

public enum Category {

    POLITICS(100),
    ECONOMY(101),
    SOCIETY(102),
    LIFESTYLE_CULTURE(103),
    WORLD(104),
    IT_SCIENCE(105);

    private final int code;

    Category(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    // 코드 번호로 해당 enum 값을 찾는 메서드
    public static Category findByCode(int code) {
        for (Category category : values()) {
            if (category.getCode() == code) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid Code: " + code);
    }

}
