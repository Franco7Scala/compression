package engine.encoding;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;


/**
 * @author francesco
 *
 */
public class GraphCoder {
	private LinkedList<Node> nodes;
	private Node source;
	private Node currentStateEncoding;
	private Node currentStateDecoding;
	private boolean encodingStarted;
	private boolean decodingStarted;
	private String description;
	
	
	public GraphCoder(Node source) {
		this.source = source;
		this.nodes = new LinkedList<>();
		encodingStarted = false;
		decodingStarted = false;
		nodes.add(source);
	}
	
	public void addLink(Link link) {
		if ( nodes.contains(link.source) && nodes.contains(link.destination) ) {
			link.source.addLink(link);
		}
		else if ( nodes.contains(link.source) && !nodes.contains(link.destination) ) {
			nodes.add(link.destination);
			link.source.addLink(link);
		}
		else if ( !nodes.contains(link.source) && nodes.contains(link.destination) ) {
			nodes.add(link.source);
			link.source.addLink(link);
		}
		else {
			nodes.add(link.source);
			nodes.add(link.destination);
			link.source.addLink(link);
		}
	}
	
	// encoding
	public void startEncoding() {
		encodingStarted = true;
		currentStateEncoding = source;
	}
	
	public void stopEncoding() {
		encodingStarted = false;
	}
	
	public BitSet getNextSymbolEncoded(BitSet input) throws Exception {
		if ( !encodingStarted ) {
			throw new Exception("You must call before startEncoding!");
		}
		for ( Link currentLink : currentStateEncoding.links ) {
			if ( currentLink.input.equals(input) ) {
				currentStateEncoding = currentLink.destination;
				return currentLink.output; 
			}
			else {
			}
		}		
		throw new Exception("Link not found!");
	}
	
	// decoding
	public void startDecoding() {
		decodingStarted = true;
		currentStateDecoding = source;
	}
	
	public void stopDecoding() {
		decodingStarted = false;
	}
	
	public BitSet getNextSymbolDecoded(BitSet output) throws Exception {
		if ( !decodingStarted ) {
			throw new Exception("You must call before startDecoding!");
		}
		Link outputLink = null;
		int minMetric = Integer.MAX_VALUE;
		for ( Link currentLink : currentStateDecoding.links ) {
			int currentMetric = currentLink.getMetric(output);
			if ( currentMetric < minMetric ) {
				outputLink = currentLink;
				minMetric = currentMetric;
			}
		}		
		if ( outputLink != null ) {
			currentStateDecoding = outputLink.destination;
			return outputLink.input;
		}
		else {
			throw new Exception("Link not found!");
		}
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		String result = "nodes: " + nodes.size() + "\n";
		for ( Node node : nodes ) {
			result = result.concat(node.toString() + "\n\n");
		}
		return result;
	}

	// inner classes
	public static class Node {
		public BitSet value;
		public ArrayList<Link> links = new ArrayList<>();
		
		
		public Node (BitSet value) {
			this.value = value;
		}
		
		public void addLink(Link link) {
				links.add(link);
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((links == null) ? 0 : links.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			String result = "NODE - value: ";
			for ( int a = 0; a < 2; a ++ ) {
				result = result.concat( (this.value.get(a)? 1 : 0) + " " );
			}
			result = result.concat("\n");
			for ( Link link : links ) {
				result = result.concat(link.toString());
			}
			return result;
		}
	}
	
	public static class Link {
		public BitSet input;
		public BitSet output;
		public Node source;
		public Node destination;
		private int cardinality;
		
		
		public Link (BitSet input, BitSet output, Node source, Node destination, int cardinality) {
			this.input = input;
			this.output = output;
			this.source = source;
			this.destination = destination;
			this.cardinality = cardinality;
		}

		public int getMetric(BitSet output) {
			int result = 0;
			for ( int i = 0; i < this.cardinality; i ++ ) {
				if ( this.output.get(i) != output.get(i) ) {
					result ++;
				}
			}
			return result;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((destination == null) ? 0 : destination.hashCode());
			result = prime * result + ((input == null) ? 0 : input.hashCode());
			result = prime * result + ((output == null) ? 0 : output.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Link other = (Link) obj;
			if (destination == null) {
				if (other.destination != null)
					return false;
			} else if (!destination.equals(other.destination))
				return false;
			if (input == null) {
				if (other.input != null)
					return false;
			} else if (!input.equals(other.input))
				return false;
			if (output == null) {
				if (other.output != null)
					return false;
			} else if (!output.equals(other.output))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			String result = "LINK\nsource     : ";
			for ( int a = 0; a < 2; a ++ ) {
				result = result.concat( (this.source.value.get(a)? 1 : 0) + " " );
			}
			result = result.concat("\ndestination: ");
			for ( int a = 0; a < 2; a ++ ) {
				result = result.concat( (this.destination.value.get(a)? 1 : 0) + " " );
			}
			result = result.concat("\ninput : ");
			for ( int a = 0; a < 1; a ++ ) {
				result = result.concat( (this.input.get(a)? 1 : 0) + " " );
			}
			result = result.concat("\noutput: ");
			for ( int a = 0; a < cardinality; a ++ ) {
				result = result.concat( (this.output.get(a)? 1 : 0) + " " );
			}
			result = result.concat("\n");
			return result;
		}
	}

	
}
