package monsterstrike.graph;

public class Vector {

    private float x;
    private float y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector genWithTwoPts(float x1, float y1, float x2, float y2) {
        return new Vector(x2 - x1, y2 - y1);
    }

    public Vector getUnitVec() { //單位向量
        return new Vector(this.x / this.getValue(), this.y / this.getValue());
    }

    public float getUnitX() { //x方向向量之單位向量
        if (this.getValue() == 0) {
            return 0f;
        }
        return this.x / this.getValue();
    }

    public float getUnitY() { //y方向向量之單位向量
        if (this.getValue() == 0) {
            return 0f;
        }
        return this.y / this.getValue();
    }

    public float getX() {  //當前x分量
        return this.x;
    }

    public float getY() { //當前y分量
        return this.y;
    }

    public Vector resizeVec(float value) { //縮放向量大小為value
        if (this.getValue() == 0) {
            return new Vector(0, 0);
        }
        return new Vector(this.getUnitX() * value, this.getUnitY() * value);
    }

    public void setX(float x) { //設定x分量
        this.x = x;
    }

    public void setY(float y) { //設定y分量
        this.y = y;
    }

    public float getValue() { //取得向量大小
        return (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public void setValue(float value) {  //設定向量大小
        if (value <= 0 || this.getValue() <= 0) {
            this.x = 0;
            this.y = 0;
        } else {
            this.x *= value / this.getValue();
            this.y *= value / this.getValue();
        }
    }

    public float getRadTheta(Vector v2) { //計算兩向量的夾角(rad)
        if (this.getValue() == 0 || v2.getValue() == 0) {
            return -1;
        }
        float cosTheta = (float) (this.dot(v2) / (this.getValue() * v2.getValue()));
        if (cosTheta >= 1) {
            return 0f;
        } else if (cosTheta <= -1) {
            return (float) Math.PI;
        }
        return (float) Math.acos(cosTheta);
    }

    public float dot(Vector v2) { //計算向量內積
        return this.x * v2.x + this.y * v2.y;
    }

    public Vector plus(Vector v2) { //向量相加
        return new Vector(this.x + v2.x, this.y + v2.y);
    }

    public Vector minus(Vector v2) { //向量相減
        return new Vector(this.x - v2.x, this.y - v2.y);
    }

    public Vector multiplyScalar(float scalor) { //向量常數積
        return new Vector(this.x * scalor, this.y * scalor);
    }

    public Vector getCosProjectionVec(Vector v2) { //投影到v2的向量
        float rad = getRadTheta(v2);
        if (rad == -1) {
            return new Vector(0, 0);
        }
        return v2.resizeVec((float) (this.getValue() * Math.cos(rad)));
    }

    public Vector getSinProjectionVec(Vector v2) { 
        if (v2.getValue() == 0) {
            return new Vector(0, 0);
        }
        return this.minus(this.getCosProjectionVec(v2));
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}
