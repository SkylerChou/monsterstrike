package collisionproject.gameobject;

public class Vector {

    private float x;
    private float y;
    private float value;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
        this.value = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    
    public Vector getUnitVec(){
        return new Vector(this.x/this.value, this.y/this.value);
    }

    public float getUnitX() {
        return this.x / this.value;
    }

    public float getUnitY() {
        return this.y / this.value;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
    
    public Vector resizeVec(float value){
        return new Vector(this.getUnitX()*value, this.getUnitY()*value);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getValue() {
        return this.value;
    }

    public float getTanX() {
        return (float) (Math.sqrt(1 / (1 + Math.pow(this.x / this.y, 2))));
    }

    public float getTanY() {
        return -(this.x / this.y) * this.getTanX();
    }
}
