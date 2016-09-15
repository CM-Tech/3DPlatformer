package game;

import java.util.ArrayList;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class RectangularPrism extends PhyObject {
	
	public RectangularPrism(Vec3 dim) {
		
		super(new Mesh(),new BoxShape(new Vector3f(dim.x/200f,dim.y/200f,dim.z/200f)),new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1f))),dim.x*dim.y*dim.z/1000000f);
		this.mesh=new Mesh();
		dim=dim.multiply(0.01f);
		Vec3 a=new Vec3(1,1,1).multiply(new Vec3(-1,1,-1)).multiply(0.5f);
		Vec3 b=new Vec3(1,1,1).multiply(new Vec3(1,1,-1)).multiply(0.5f);
		Vec3 c=new Vec3(1,1,1).multiply(new Vec3(1,1,1)).multiply(0.5f);
		Vec3 d=new Vec3(1,1,1).multiply(new Vec3(-1,1,1)).multiply(0.5f);
		//top
		this.mesh.addTri(a,b,c);
		this.mesh.addTri(c,d,a);
		a.rotate("x", Vec3.ZERO, 90);
		b.rotate("x", Vec3.ZERO, 90);
		c.rotate("x", Vec3.ZERO, 90);
		d.rotate("x", Vec3.ZERO, 90);
		this.mesh.addTri(a,b,c);
		this.mesh.addTri(c,d,a);
		a.rotate("x", Vec3.ZERO, 90);
		b.rotate("x", Vec3.ZERO, 90);
		c.rotate("x", Vec3.ZERO, 90);
		d.rotate("x", Vec3.ZERO, 90);
		this.mesh.addTri(a,b,c);
		this.mesh.addTri(c,d,a);
		a.rotate("x", Vec3.ZERO, 90);
		b.rotate("x", Vec3.ZERO, 90);
		c.rotate("x", Vec3.ZERO, 90);
		d.rotate("x", Vec3.ZERO, 90);
		this.mesh.addTri(a,b,c);
		this.mesh.addTri(c,d,a);
		
		a.rotate("y", Vec3.ZERO, 90);
		b.rotate("y", Vec3.ZERO, 90);
		c.rotate("y", Vec3.ZERO, 90);
		d.rotate("y", Vec3.ZERO, 90);
		this.mesh.addTri(a,b,c);
		this.mesh.addTri(c,d,a);
		
		a.rotate("y", Vec3.ZERO, 180);
		b.rotate("y", Vec3.ZERO, 180);
		c.rotate("y", Vec3.ZERO, 180);
		d.rotate("y", Vec3.ZERO, 180);
		this.mesh.addTri(a,b,c);
		this.mesh.addTri(c,d,a);
		
		Vec3[][] tranTris= this.mesh.getTris();
		Vec3[][] tranTrisD=new Vec3[tranTris.length][3];
		for (int i = 0; i < tranTris.length; i++) {
			tranTrisD[i]=new Vec3[3];
			for (int m = 0; m < 3; m++) {
				tranTrisD[i][m]=tranTris[i][m].multiply(dim);
			
			}
		}
		this.mesh.triArray=tranTrisD;
		//this.collider.mesh.triArray=this.mesh.triArray.clone();
		
		
		// TODO Auto-generated constructor stub
	}

	

}
