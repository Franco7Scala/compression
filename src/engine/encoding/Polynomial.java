package engine.encoding;


import java.util.BitSet;

import engine.encoding.GraphCoder.Link;
import engine.encoding.GraphCoder.Node;

/**
 * @author francesco
 *
 */
public class Polynomial {
//TODO mettere cose Encoder paramet

	public static EncoderParameters get4StatesPolynomial() {
		EncoderParameters result = new EncoderParameters();
		int cardinality = 2;
		Node n0 = new Node(BitSet.valueOf( new byte[] {0} ));
		Node n1 = new Node(BitSet.valueOf( new byte[] {1} ));
		Node n2 = new Node(BitSet.valueOf( new byte[] {2} ));
		Node n3 = new Node(BitSet.valueOf( new byte[] {3} ));	
		GraphCoder graph = new GraphCoder(n0);
		// S0
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {0} ), n0, n0, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {3} ), n0, n1, cardinality));
		// S1
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {1} ), n1, n2, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {2} ), n1, n3, cardinality));
		// S2
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {3} ), n2, n0, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {0} ), n2, n1, cardinality));
		// S3
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {2} ), n3, n2, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {1} ), n3, n3, cardinality));		
		// description
		graph.setDescription("This is a convolutional encoder (with 4 states):\n"
						    + "- Binary rate: 1/2\n"
				            + "- K: 3\n"
	                        + "- Polynomial: [1+D^2,   1+D+D^2]");
		result.coder = graph;
		return result;
	}
	
	public static EncoderParameters get8StatesPolynomial() {
		EncoderParameters result = new EncoderParameters();
		int cardinality = 2;
		Node n0 = new Node(BitSet.valueOf( new byte[] {0} ));
		Node n1 = new Node(BitSet.valueOf( new byte[] {1} ));
		Node n2 = new Node(BitSet.valueOf( new byte[] {2} ));
		Node n3 = new Node(BitSet.valueOf( new byte[] {3} ));
		Node n4 = new Node(BitSet.valueOf( new byte[] {4} ));
		Node n5 = new Node(BitSet.valueOf( new byte[] {5} ));
		Node n6 = new Node(BitSet.valueOf( new byte[] {6} ));
		Node n7 = new Node(BitSet.valueOf( new byte[] {7} ));
		GraphCoder graph = new GraphCoder(n0);
		// S0
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {0} ), n0, n0, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {3} ), n0, n4, cardinality));
		// S1
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {3} ), n1, n0, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {0} ), n1, n4, cardinality));
		// S2
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {2} ), n2, n1, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {1} ), n2, n5, cardinality));
		// S3
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {1} ), n3, n1, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {2} ), n3, n5, cardinality));	
		// S4
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {3} ), n4, n2, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {0} ), n4, n6, cardinality));
		// S5
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {0} ), n5, n2, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {3} ), n5, n6, cardinality));
		// S6
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {1} ), n6, n3, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {2} ), n6, n7, cardinality));
		// S7
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {2} ), n7, n3, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {3} ), n7, n7, cardinality));	
		// description
		graph.setDescription("This is a convolutional encoder (with 8 states):\n"
				            + "- Binary rate: 1/2\n"
				            + "- K: 4\n"
	                        + "- Polynomial: [1+D+D^3,   1+D+D^2]");
		result.coder = graph;
		return result;
	}

	public static EncoderParameters get16StatesPolynomial() {
		EncoderParameters result = new EncoderParameters();
		int cardinality = 4;
		Node n0 = new Node(BitSet.valueOf( new byte[] {0} ));
		Node n1 = new Node(BitSet.valueOf( new byte[] {1} ));
		Node n2 = new Node(BitSet.valueOf( new byte[] {2} ));
		Node n3 = new Node(BitSet.valueOf( new byte[] {3} ));
		Node n4 = new Node(BitSet.valueOf( new byte[] {4} ));
		Node n5 = new Node(BitSet.valueOf( new byte[] {5} ));
		Node n6 = new Node(BitSet.valueOf( new byte[] {6} ));
		Node n7 = new Node(BitSet.valueOf( new byte[] {7} ));
		Node n8 = new Node(BitSet.valueOf( new byte[] {8} ));
		Node n9 = new Node(BitSet.valueOf( new byte[] {9} ));
		Node n10 = new Node(BitSet.valueOf( new byte[] {10} ));
		Node n11 = new Node(BitSet.valueOf( new byte[] {11} ));
		Node n12 = new Node(BitSet.valueOf( new byte[] {12} ));
		Node n13 = new Node(BitSet.valueOf( new byte[] {13} ));
		Node n14 = new Node(BitSet.valueOf( new byte[] {14} ));
		Node n15 = new Node(BitSet.valueOf( new byte[] {15} ));
		GraphCoder graph = new GraphCoder(n0);
		// S0
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {0} ), n0, n0, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {0} ), n0, n4, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {0} ), n0, n1, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {0} ), n0, n5, cardinality));
		// S1
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {4} ), n1, n0, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {4} ), n1, n4, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {4} ), n1, n1, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {4} ), n1, n5, cardinality));
		// S2
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {7} ), n2, n2, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {7} ), n2, n6, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {7} ), n2, n3, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {7} ), n2, n7, cardinality));
		// S3
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {2} ), n3, n2, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {2} ), n3, n6, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {2} ), n3, n3, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {2} ), n3, n7, cardinality));
		// S4
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {3} ), n4, n0, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {3} ), n4, n4, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {3} ), n4, n1, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {3} ), n4, n5, cardinality));
		// S5
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {6} ), n5, n0, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {6} ), n5, n4, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {6} ), n5, n1, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {6} ), n5, n5, cardinality));
		// S6
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {4} ), n6, n2, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {4} ), n6, n6, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {4} ), n6, n3, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {4} ), n6, n7, cardinality));
		// S7
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {1} ), n7, n2, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {1} ), n7, n6, cardinality));	
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {1} ), n7, n3, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {1} ), n7, n7, cardinality));
		// S8
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {4} ), n8, n8, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {4} ), n8, n12, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {4} ), n8, n9, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {4} ), n8, n13, cardinality));
		// S9
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {1} ), n9, n8, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {1} ), n9, n12, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {1} ), n9, n9, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {1} ), n9, n13, cardinality));
		// S10
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {3} ), n10, n10, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {3} ), n10, n14, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {3} ), n10, n11, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {3} ), n10, n15, cardinality));
		// S11
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {6} ), n11, n10, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {6} ), n11, n14, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {6} ), n11, n11, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {6} ), n11, n15, cardinality));
		// S12
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {7} ), n12, n8, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {7} ), n12, n12, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {7} ), n12, n9, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {7} ), n12, n13, cardinality));
		// S13
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {2} ), n13, n8, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {2} ), n13, n12, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {2} ), n13, n9, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {2} ), n13, n13, cardinality));
		// S14
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {0} ), n14, n10, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {0} ), n14, n14, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {0} ), n14, n11, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {0} ), n14, n15, cardinality));
		// S15
		graph.addLink(new Link(BitSet.valueOf( new byte[] {0} ), BitSet.valueOf( new byte[] {5} ), n15, n10, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {1} ), BitSet.valueOf( new byte[] {5} ), n15, n14, cardinality));	
		graph.addLink(new Link(BitSet.valueOf( new byte[] {2} ), BitSet.valueOf( new byte[] {5} ), n15, n11, cardinality));
		graph.addLink(new Link(BitSet.valueOf( new byte[] {3} ), BitSet.valueOf( new byte[] {5} ), n15, n15, cardinality));
		// description
		graph.setDescription("This is a convolutional encoder (with 16 states):\n"
							+ "- Binary rate: 2/3\n"
							+ "- K: 3\n"
				            + "- Polynomial: [D+D^2,   D^2,   D+D^2]\n"
				            + "                       [D^2,   D,   D]");
		result.coder = graph;
		return result;
	}
	

}
