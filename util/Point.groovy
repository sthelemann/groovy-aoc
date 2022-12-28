class Point {
    int x
    int y

    Point(x, y) {
        this.x = x
        this.y = y
    }

    boolean equals(p) {
        this.x == p.x && this.y == p.y
    }

    String toString() {
        "($this.x, $this.y)".toString()
    }
}
