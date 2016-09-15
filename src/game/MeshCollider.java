package game;

import java.io.Serializable;

public class MeshCollider implements Serializable{
	public Mesh mesh=new Mesh();
	
	public Vec3 worldPos=new Vec3(0,0,0);
	public MeshCollider(){
		mesh=new Mesh();
	}
public MeshCollider(Mesh m){
	mesh=m;
	}
	public boolean intersects(MeshCollider other){
		return this.mesh.intersects(other.mesh,this.worldPos,other.worldPos);
	}
}
