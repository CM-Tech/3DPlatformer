package game;

import java.io.Serializable;
import java.util.ArrayList;

public class Mesh  implements Serializable{
	public Vec3[][] triArray=new Vec3[0][3];
	public Mesh(){
		
	}
	public Mesh(Vec3[][] tris){
		triArray=new Vec3[tris.length][3];
		for(int i=0;i<tris.length;i++){
			this.triArray[i]=new Vec3[3];
			for(int j=0;j<3;j++){
				this.triArray[i][j]=tris[i][j].clone();
			}
		}
	}
	public Mesh clone(){
		return new Mesh(this.triArray);
	}
	public boolean intersects(Mesh other,Vec3 off1,Vec3 off2){
		Vec3[][] triArray2=other.getTris();
		for(int i=0;i<triArray.length;i++){
			for(int j=0;j<triArray2.length;j++){
				if(IntersectionUtils.intersectTriangleTriangle(triArray[i][0].add(off1), triArray[i][1].add(off1), triArray[i][2].add(off1), triArray2[j][0].add(off2), triArray2[j][1].add(off2), triArray2[j][2].add(off2))){
					return true;
				}
			}
		}
		return false;
	}
	public void addTri(Vec3 a,Vec3 b,Vec3 c){
		//ArrayList<ArrayList<Vec3>> n= new ArrayList<ArrayList<Vec3>>();
		Vec3[][] newTriArray=new Vec3[triArray.length+1][3];
		for(int i=0;i<triArray.length;i++){
			newTriArray[i]=triArray[i];
			
		}
		newTriArray[newTriArray.length-1]=new Vec3[]{a.clone(),b.clone(),c.clone()};
		this.triArray=newTriArray;
	}
	public void removeTri(int index){
		//ArrayList<ArrayList<Vec3>> n= new ArrayList<ArrayList<Vec3>>();
		Vec3[][] newTriArray=new Vec3[triArray.length-1][3];
		int j=0;
		for(int i=0;i<triArray.length;i++){
			if(i!=index){
			newTriArray[j]=triArray[i];
			j++;
			}
			
		}
		
		triArray=newTriArray;
	}
	public Vec3[][] getTris(){
		return triArray.clone();
	}

}
