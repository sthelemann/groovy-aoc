class Point {
    int x
    int y

    Point(x, y) {
        this.x = x
        this.y = y
    }

    String toString() {
        "($this.x, $this.y)".toString()
    }
}
