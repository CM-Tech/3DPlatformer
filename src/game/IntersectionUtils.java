package game;

public class IntersectionUtils {
	// for triangle a-b-c, intersect the line made by p-q and store intersection point in r
	// returns true if intersected, or false if no intersection occurs
	public static boolean intersectLineTriangle(Vec3 p, Vec3 q, Vec3 a, Vec3 b, Vec3 c, Vec3 r) {
	   // Bring points to their respective coordinate frame
	   Vec3 pq = q.subtract( p);
	   Vec3 pa =a.subtract(p);
	   Vec3 pb = b.subtract(p);
	   Vec3 pc = c.subtract(p);
	   
	   Vec3 m = pq.cross(pc);
	   
	   float u = pb.dot(m);      
	   float v = -pa.dot(m);
	   
	   if (Math.signum(u) != Math.signum(v)) {
	      return false;
	   }
	   
	   // scalar triple product
	   float w = pq.dot( pb.cross(pa));
	   
	   if (Math.signum(u) != Math.signum(w)) {
	      return false;
	   }
	   
	   float denom = 1.0f / (u + v + w);
	   
	   // r = ((u * denom) * a) + ((v * denom) * b) + ((w * denom) * c);
	   Vec3 compA = a.multiply(u * denom);
	   Vec3 compB = b.multiply( v * denom);
	   Vec3 compC = c.multiply( w * denom);
	   
	   // store result in Vector r
	   r.x = compA.x + compB.x + compC.x;
	   r.y = compA.y + compB.y + compC.y;
	   r.z = compA.z + compB.z + compC.z;
	   
	   return true;
	}
	public static boolean intersectTriangleTriangle( Vec3 a, Vec3 b, Vec3 c, Vec3 q,Vec3 r, Vec3 s) {
		Vec3 pH=new Vec3(0,0,0);
		  if( intersectLineTriangle(a,b,q,r,s,pH)){
			  return true;
		  }
		  if( intersectLineTriangle(a,c,q,r,s,pH)){
			  return true;
		  }
		  if( intersectLineTriangle(c,b,q,r,s,pH)){
			  return true;
		  }
		  if( intersectLineTriangle(q,r,a,b,c,pH)){
			  return true;
		  }
		  if( intersectLineTriangle(q,s,a,b,c,pH)){
			  return true;
		  }
		  if( intersectLineTriangle(s,r,a,b,c,pH)){
			  return true;
		  }
		   return false;
		}
}
