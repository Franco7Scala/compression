package encoding;

import java.util.BitSet;

import encoding.GraphCoder.Link;
import encoding.GraphCoder.Node;

/**
 * @author francesco
 *
 */
public class Polynomial {

	
	public static GraphCoder getSimplePolynomial () {
		int cardinality = 2;
		Node n0 = new Node(BitSet.valueOf( new byte[] {0} ));
		Node n1 = new Node(BitSet.valueOf( new byte[] {1} ));
		Node n2 = new Node(BitSet.valueOf( new byte[] {2} ));
		Node n3 = new Node(BitSet.valueOf( new byte[] {3} ));
		
		GraphCoder result = new GraphCoder(n0);
		// S0
		result.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {0} ), n0, n0, cardinality));
		result.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {3} ), n0, n1, cardinality));
		// S1
		result.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {1} ), n1, n2, cardinality));
		result.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {2} ), n1, n3, cardinality));
		// S2
		result.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {3} ), n2, n0, cardinality));
		result.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {0} ), n2, n1, cardinality));
		// S3
		result.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {2} ), n3, n2, cardinality));
		result.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {1} ), n3, n3, cardinality));		
		// description
		result.setDescription("This is simple polynomial for test:\n[1 + D^2, 1 + D + D^2]");
		return result;
	}
	
	
}
