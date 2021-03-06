package topiaryexplorer;

/**
 * Represents a node in the tree, which may be a leaf or internal node.
 */


import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Node implements Comparable{
  private Node parent = null;
  public ArrayList<Node> nodes = new ArrayList(); //children
  private ArrayList<Node> anscestors = new ArrayList();
  private HashSet pruneVals = new HashSet();
  public ArrayList<Boolean> pruneStates = new ArrayList<Boolean>();

  private String label = "";
  private String name = "";
  private String lineage = "";
  private String consensusLineage = null;
  private String tconsensusLineage = null;
  private double branchlength = 0;
  
  private double depth = 0;
  private int numberOfLeaves = 0;
  // private int numOtusCovered = 0;
  
  private double labelyoffset = 0;
  private double labelxoffset = 0;
  
  private double yoffset = 0;
  private double xoffset = 0;
  private double roffset = 0; //radius
  private double toffset = 0; //theta
  private double rxoffset = 0;
  private double ryoffset = 0;
  private boolean drawPie = false; //should a pie chart be drawn for this node?
  private boolean drawLabel = true;
  private boolean locked = false;
  private double maximumYOffset = 0;
  private double minimumYOffset = 0;
  private double maximumTOffset = 0;
  private double minimumTOffset = 0;
  private double maximumROffset = 0;
  private double minimumROffset = 0;
  private double maximumRXOffset = 0;
  private double minimumRXOffset = 0;
  
  //parallel arrays of colors and the weight to be drawn with each
  private boolean branchColored = true;
  HashMap branchColorMap = new HashMap();
  private Color branchColor = null;
  private ArrayList<Color> groupBranchColor = new ArrayList<Color>();
  private ArrayList<Double> groupBranchWeight = new ArrayList<Double>();
  private ArrayList<String> groupBranchValue = new ArrayList<String>();
  
  private boolean labelColored = false;
  HashMap labelColorMap = new HashMap();
  private Color labelColor = null;
  private ArrayList<Color> groupLabelColor = new ArrayList<Color>();
  private ArrayList<Double> groupLabelWeight = new ArrayList<Double>();
  private ArrayList<String> groupLabelValue = new ArrayList<String>();

  private Color normalizedBranchColor = null;
  
  private double totalAbundance = 0;
  private double lineWidth = 1;

  // if true, the children are not shown (draws a wedge)
  private boolean collapsed = false; 
  private boolean sliderCollapsed = true;
  private boolean hidden = false;
  
  private boolean hover = false;
  
  private boolean toPrune = false;
  
  private Point2D point = new Point2D.Double(0.0,0.0);

  Object userObject = null;
  String userString = "";


  Node() {}

  Node(String _label, String _name, double _branchlength) {
    label = _label;
    name = _name;
    branchlength = _branchlength;
  }
  
  Node(Node parent) {
      branchlength = 0.0;
      parent = parent;
      nodes = new ArrayList<Node>();
  }
  
  Node(String _name, String _label, double _branchlength, ArrayList<Node> _nodes,
   Node _parent, String _consensusLineage) {
      name = _name;
      label = _label;
      branchlength = _branchlength;
      nodes = new ArrayList<Node>();
      for(Node n : _nodes)
          nodes.add(new Node(n.getName(), n.getLabel(), n.getBranchLength(), n.nodes, n.getParent(), n.getConsensusLineage()));
          
      parent = _parent;//new Node(_parent.getName(), _parent.getLabel(), _parent.getBranchLength(), _parent.nodes, _parent.getParent(), _parent.getConsensusLineage());
      setConsensusLineage(_consensusLineage);
      // branchColor = branchColor;
      // labelColor = _labelColor;
  }
  
  //GETTERS AND SETTERS
  public void setPoint(Point2D p) {point = p;}
  public Point2D getPoint() { return point; }
  public Color getBranchColor() { if(branchColor!= null) return branchColor; return new Color(0); }
  public Color getLabelColor() { return labelColor; }
  public boolean isHidden() { return hidden; }
  public void setHidden(boolean cond) { hidden = cond; }
  public boolean isLocked() { return locked; }
  public boolean isLeaf() { return nodes.size() == 0; }
  public void setDrawPie(boolean b) { drawPie = b; }
  public boolean getDrawPie() { return drawPie; }
  public void setDrawLabel(boolean b) { drawLabel = b; }
  public boolean getDrawLabel() { return drawLabel; }
  public ArrayList<String> getGroupBranchValue() { return groupBranchValue; }
  public ArrayList<Double> getGroupBranchWeight() { return groupBranchWeight; }
  public ArrayList<Color> getGroupBranchColor() { return groupBranchColor; }
  public ArrayList<Double> getGroupBranchFraction() { return groupBranchWeight; }
  public ArrayList<String> getGroupLabelValue() { return groupLabelValue; }
  public ArrayList<Color> getGroupLabelColor() { return groupLabelColor; }
  public ArrayList<Double> getGroupLabelFraction() { return groupLabelWeight; }
  public boolean isCollapsed() { return collapsed || sliderCollapsed; }
  public void setSliderCollapsed(boolean cond) { if(!locked) sliderCollapsed = cond; }
  public void setCollapsed(boolean cond) { if(!locked && parent != null) collapsed = cond; }
  public void setLabel(String s) { label = s; }
  public String getLabel() { return label; }
  public double getBranchLength() { return branchlength; }
  public void setName(String s) { name = s; label = s; }
  public String getName() { return name; }
  public void setLineage(String s) { 
      if(s.charAt(s.length()-1) == ';')
      s = s.substring(0,s.length()-1);
    lineage = s; }
  public String getLineage() { 
      if(!lineage.equals("Unclassified-Screened"))
          return lineage;
        return null; 
        }
  public void setConsensusLineage(String s) { consensusLineage = s; }
  public String getConsensusLineage() { return consensusLineage;}
  public void setBranchLength(double f) { if (f >= 0) branchlength = f; }
  public double getYOffset() { return yoffset; }
  public double getXOffset() { return xoffset; }
  public void setYOffset(double f) { yoffset = f; }
  public void setXOffset(double f) { xoffset = f; }
  public double getROffset() { return roffset; }
  public double getTOffset() { return toffset; }
  public void setROffset(double f) { roffset = f; }
  public void setTOffset(double f) { toffset = f; }  
  public double getRXOffset() { return rxoffset; }
  public double getRYOffset() { return ryoffset; }
  public void setRXOffset(double f) { rxoffset = f; }
  public void setRYOffset(double f) { ryoffset = f; }   
  public Node getParent() { return parent; }
  public double getMaximumYOffset() { return maximumYOffset; }
  public void setMaximumYOffset(double f) { maximumYOffset = f; }
  public double getMinimumYOffset() { return minimumYOffset; }
  public void setMinimumYOffset(double f) { minimumYOffset = f; }
  public double getMaximumTOffset() { return maximumTOffset; }
  public void setMaximumTOffset(double f) { maximumTOffset = f; }
  public double getMinimumTOffset() { return minimumTOffset; }
  public void setMinimumTOffset(double f) { minimumTOffset = f; }
  public double getMaximumROffset() { return maximumROffset; }
  public void setMaximumROffset(double f) { maximumROffset = f; }
  public double getMinimumROffset() { return minimumROffset; }
  public void setMinimumROffset(double f) { minimumROffset = f; }  
  public double getMinimumRXOffset() { return minimumRXOffset; }
  public void setMinimumRXOffset(double f) { minimumRXOffset = f; }    
  public double getMaximumRXOffset() { return maximumRXOffset; }
  public void setMaximumRXOffset(double f) { maximumRXOffset = f; }  
  public double getTotalAbundance() { return totalAbundance; }
  public void setTotalAbundance(double f) { totalAbundance = f; }
  public double getLineWidth() { return lineWidth; }
  public void setLineWidth(double f) { lineWidth = f; }
  public void setAnscestors(ArrayList<Node> a) { anscestors = a; }
  public ArrayList<Node> getAnscestors() { return anscestors; }
  public int getLevel() { return anscestors.size(); }
  public void setDepth(double val) { depth = val;}
  public double depth() { return depth; }
  public void setNumberOfLeaves(int val) { numberOfLeaves = val;}
  public int getNumberOfLeaves() { return numberOfLeaves; }
  public void setHover(boolean b) { hover = b; }
  public boolean getHover() { return hover; }
  public void setLabelYOffset(double d) { labelyoffset = d; }
  public double getLabelYOffset() { return labelyoffset; }
  public void setLabelXOffset(double d) { labelxoffset = d; }
  public double getLabelXOffset() { return labelxoffset; }
  public void setParent(Node p) { parent = p; }
  
  public boolean isInternal() {
      return this.nodes.size()>1;
  }
  
  // public Node unrootedDeepcopy(Node parent) {
  //     ArrayList<Node> children = new ArrayList<Node>();
  //     if(parent != null) {
  //     for(Node n : nodes)
  //       children.add(n.unrootedDeepcopy(this));
  //     }
  //     
  //     Node edge = new Node();
  //     if(parent == null)
  //       edge = null;
  //     else if(parent.parent == this)
  //       edge = parent;
  //     else { 
  //     // if(parent == this.parent)
  //       edge = this;
  //     }
  //     
  //     if(edge == null)
  //       edge = new Node("root","root",1.0);
  //     edge.nodes = children;
  //     return edge;
  // }
  
  public int compareTo(Object otherNode) throws ClassCastException {
      if (!(otherNode instanceof Node))
        throw new ClassCastException("A Node object expected.");
      int otherNumChildren = ((Node)otherNode).getLeaves().size();
      int thisNumChildren = this.getLeaves().size();
      if(thisNumChildren > otherNumChildren)
      {
          return 1;
      }
      else if(thisNumChildren < otherNumChildren)
      {
          return -1;
      }
      else
        return 0;
  }
  
  public void ladderize(){
      if(isLeaf())
        return;
      Collections.sort(nodes);
      for(Node n: nodes)
        n.ladderize();
  }
  
  public void prune(double total, double perc) {
      // if branch length is larger than val to prune by, return
      if(getBranchLength()/total > perc)
        return;
      
      if(parent == null)
        return;
      
      // otherwise prune it
      // parent.setName(parent.getName() +"_"+ this.name);
      toPrune = true;
      parent.nodes.remove(this);
  }
  
  public void prune() {
      ArrayList<Node> nodes_to_remove = new ArrayList<Node>();
      
      for(Node node : getNodes())
      {
          boolean prune = node.toPrune;
          if(node.pruneStates.size() > 0)
          {
            for(boolean p : node.pruneStates)
                {
                    prune = prune && p;
                }
          }
          if(prune)
            node.parent.nodes.remove(node);
          
          node.toPrune = prune;
          node.pruneStates = new ArrayList<Boolean>();
      }
      
      for(Node node : getNodes())
      {            
        if(node.parent == null)
          continue;
        if(node.toPrune && node.nodes.size() == 1)
          nodes_to_remove.add(node);
      }
      
      for(Node node : nodes_to_remove)
      {
          Node curr_parent = node.parent;
          Node child = node.nodes.get(0);
          curr_parent.nodes.remove(node);
          node.parent = null;
          curr_parent.nodes = new ArrayList<Node>();
          curr_parent.nodes.add(child);
          child.parent = curr_parent;
          child.setBranchLength(child.getBranchLength()+node.getBranchLength());
      }
      
      for(Node node : getNodes())
      {
          if(node.nodes.size() == 1)
          {
              node.setBranchLength(node.getBranchLength()+node.nodes.get(0).getBranchLength());
              node.setName(node.nodes.get(0).getName());
              node.setLabel(node.nodes.get(0).getLabel());
              node.nodes = node.nodes.get(0).nodes;
          }
      }
        // otherwise, prune this node
        // if(toPrune)
        // {
        //     parent.setName(parent.getName() +"_"+ this.name);
        //     parent.nodes.remove(this);
        //     return;
        // }
        // 
        // if(parent.nodes.size() == 1)
        // {
        //     parent.setBranchLength(parent.getBranchLength()+nodes.get(0).getBranchLength());
        //     parent.setName(nodes.get(0).getName());
        //     parent.setLabel(nodes.get(0).getLabel());
        //     parent.nodes = new ArrayList<Node>();
        //     return;
        // }
        // 
        // // also prune all of this nodes' children recursively
        // ArrayList<Node> children = new ArrayList<Node>(nodes);
        // for(Node c : children)
        //   c.prune();
    }
  
  public boolean remove(Node n) {
      String target = "";
      if(n instanceof Node)
        target = n.getName();
      for(int i = 0; i < nodes.size(); i++)
        if(nodes.get(i).getName() == target)
        {
            removeNode(nodes.get(i));
            return true;
        }
      return false;
  }
  
  public boolean removeNode(Node n) {
      int to_delete = -1;
     for(int i = 0; i < nodes.size(); i++)
        if(nodes.get(i) == n)
        {
            to_delete = i;
            break;
        }
    if(to_delete == -1)
        return false;
    else
    {
        nodes.remove(to_delete);
        return true;
    }
  }
  
  public void prune(boolean b) {        
      // prune this node
      toPrune = b;
      pruneStates.add(b);
  }
  
  public void prune(boolean p, Object k, Object v) {
        // if(parent == null)
          // return;
        // if this node contains values that were not set to prune
        // then pruneVals will contain different values
        // pruneVals.add(v.toString());
        // pruneVals.add(k.toString());
        toPrune = p;
        pruneStates.add(p);
        // set this node to be pruned
        // toPrune = p;
        // check siblings
        // for(Node s : parent.nodes)
        // {
            // if(!s.toPrune)
              // return;
        // }
        // if all siblings were pruned, prune parent
        // parent.prune(p);
    }
  
  public void setLocked(boolean l) { 
      locked=l; 
      for(Node n: nodes)
        n.setLocked(l);
      }
  
  // recursive method to return consensus lineage
  public String getConsensusLineageF(double perc) {   
      // If the node is a leaf return lineage
      if(isLeaf())
      {
          if(!lineage.equals("Unclassified-Screened"))
            return lineage;
          return null;
      }
      
      ArrayList<Node> tips = getLeaves();
      
      // Collect lineage of tips
      ArrayList<String> currLabels = new ArrayList<String>();
      for(Node n: tips)
      {
          String l = n.getLineage();
          if(l != null)
              currLabels.add(l);
      }
      
      if(currLabels.size() == 0)
        return null;
      
      String consensusLineage = "";
      boolean loop = true;
      
      while(currLabels.size() > 0)
      {
          ArrayList<String> curr = new ArrayList<String>();
          ArrayList<String> newLabels = new ArrayList<String>();
          for(String l: currLabels)
          {
              l = l.trim().replace("\"", "");
              if(l.indexOf(";") == -1)
                curr.add(l);
              else 
              {
                  curr.add(l.substring(0,l.indexOf(";")));
                  newLabels.add(l.substring(l.indexOf(";")+1, l.length()).trim());   
              }  
          }
    
          String c = TopiaryFunctions.getConsensus(curr,perc);
          if(c != null)
            consensusLineage += c + ";";

          currLabels = newLabels;
      }
      return consensusLineage;
  }
  
  // recursive method to return consensus lineage for a 
  // specific taxonomic level
  public String getConsensusLineageF(double perc, int level) {
      // If the node is a leaf return lineage
      if(isLeaf())
      {
          String lineageAtLevel = "";
          if(lineage.split(";").length-1 >= level)
              lineageAtLevel = lineage.split(";")[level].trim().replace("\"", "");
              
          if(!lineageAtLevel.equals("Unclassified-Screened"))
            return lineageAtLevel;
          return null;
      }
      
      ArrayList<Node> tips = getLeaves();
      
      // Collect lineage of tips
      ArrayList<String> currLabels = new ArrayList<String>();
      for(Node n: tips)
      {
          String[] classifications = n.getLineage().split(";");
          if(classifications.length-1 >= level)
              currLabels.add(classifications[level].trim().replace("\"", ""));
      }
      
      if(currLabels.size() == 0)
        return null;
      
      String consensusLineage = TopiaryFunctions.getConsensus(currLabels,perc);
      
      return consensusLineage;
  }
  
  public int getNumberOfLeavesF() {
      int total = 0;
      if (isLeaf()) {
          total = 1;
      } else {
          for (Node n : nodes) {
              total = total + n.getNumberOfLeaves();
          }
      }
      numberOfLeaves = total;
      return total;
  }

  /**
   * Based on the groupBranchWeight and groupBranchColor field, return an overall blended color
   */
 public Color getBranchColor(boolean majority) {        
     //if there's no color, use black
     if (!branchColored) {
         return new Color(0);
     }
     double r,g,b;
     r = g = b = 0;
     
     if(majority)
     {
        double max = Double.MIN_VALUE;
        // Color majorityColor = new Color(0);
		ArrayList<Color> majorityColors = new ArrayList<Color>();
         
         if(groupBranchColor.size() == 1 || groupBranchWeight.size() == 1)
            return groupBranchColor.get(0);

         for(int i = 0; i < groupBranchColor.size(); i++)
         {
             if(groupBranchWeight.get(i) > max)
             {
				majorityColors = new ArrayList<Color>();
                majorityColors.add(groupBranchColor.get(i));
                max = groupBranchWeight.get(i);
				 
             }
			else if(groupBranchWeight.get(i) == max)
			{
				majorityColors.add(groupBranchColor.get(i));
			}
         }
		
		double total = majorityColors.size();
		for(Color c : majorityColors)
		{
			r += c.getRed()/total;
			g += c.getGreen()/total;
			b += c.getBlue()/total;
		}
		
         branchColor = new Color(Math.abs((float)r/255),Math.abs((float)g/255),Math.abs((float)b/255));
     }
     else {
        double total = 0;
        for (Double weight : groupBranchWeight) {
			if(weight > 0)
          		total = total + weight;
        }
        for (int i = 0; i < groupBranchWeight.size(); i++) {
          r += groupBranchWeight.get(i)/total*groupBranchColor.get(i).getRed();
          g += groupBranchWeight.get(i)/total*groupBranchColor.get(i).getGreen();
          b += groupBranchWeight.get(i)/total*groupBranchColor.get(i).getBlue();
        }
        
        if(total == 0 && groupBranchColor.size() != 0)
			branchColor = groupBranchColor.get(0);
        else if(groupBranchColor.size() != 0)
            branchColor = new Color(Math.abs((float)r/255),Math.abs((float)g/255),Math.abs((float)b/255));
		else if(groupBranchColor.size() == 0)
			branchColor = new Color(0);
    }
    return branchColor;
  }
  
  public void setNormalizedBranchColor(Color c) {
	  normalizedBranchColor = c;
  }
  
  public Color getNormalizedBranchColor() {
	  if(normalizedBranchColor == null)
		  return getBranchColor(true);
	  return normalizedBranchColor;
  }
  
  public void noBranchColor() {
    branchColored = false;
  }

  public void clearBranchColor() {
    branchColor = new Color(0);
    groupBranchWeight = new ArrayList<Double>();
    groupBranchColor = new ArrayList<Color>();
    groupBranchValue = new ArrayList<String>();
  }

  public void addBranchColor(Color c, double w) {
      branchColored = true;
      branchColorMap.put(c,w);
      groupBranchWeight.add(new Double(w));
      groupBranchColor.add(c);
  }
  
  public void addBranchValue(Object v) {
      groupBranchValue.add(v.toString());
  }
  
  public Color getLabelColor(boolean majority) {        
       //if there's no color, use black
       if (!labelColored) {
           return new Color(0,0,0);
       }
       double r,g,b;
       r = g = b = 0;

       if(majority)
       {
           double max = -100;
           Color majorityColor = new Color(0);
           if(groupLabelColor.size() == 0)
             return new Color(0,0,0);
           if(groupLabelColor.size() == 1)
             return groupLabelColor.get(0);

           for(int i = 0; i < groupLabelColor.size(); i++)
           {
               if(groupLabelWeight.get(i) > max)
               {
                   majorityColor = groupLabelColor.get(i);
                   max = groupLabelWeight.get(i);
               }
           }
           labelColor = majorityColor;
       }
       else {
          double total = 0;
          for (Double weight : groupLabelWeight) {
            total = total + weight;
          }
          for (int i = 0; i < groupBranchWeight.size(); i++) {
            r += groupLabelWeight.get(i)/total*groupLabelColor.get(i).getRed();
            g += groupLabelWeight.get(i)/total*groupLabelColor.get(i).getGreen();
            b += groupLabelWeight.get(i)/total*groupLabelColor.get(i).getBlue();
          }
          labelColor = new Color(Math.abs((float)r/255),Math.abs((float)g/255),Math.abs((float)b/255));
      }
      return labelColor;
    }

    public void noLabelColor() {
      labelColored = false;
    }

    public void clearLabelColor() {
      labelColor = new Color(0,0,0);
      groupLabelWeight = new ArrayList<Double>();
      groupLabelColor = new ArrayList<Color>();
      groupLabelValue = new ArrayList<String>();
    }

    public void addLabelColor(Color c, double w) {
        labelColored = true;
        labelColorMap.put(c,w);
        groupLabelWeight.add(new Double(w));
        groupLabelColor.add(c);
    }
  
    public void addLabelValue(Object v) {
        groupLabelValue.add(v.toString());
    }
  
  /**
   * Returns a list of all the leaves of the tree
   */
  public ArrayList<Node> getLeaves() {
      ArrayList<Node> result = new ArrayList<Node>();

      if (isLeaf()) {
          result.add(this);
          return result;
      }
      
      for (Node n : nodes) {
          result.addAll(n.getLeaves());
      }
      return result;
  }
  
    /**
   * Returns a list of all the names of leaves of the tree
   */
  public ArrayList<String> getLeafNames() {
      ArrayList<String> result = new ArrayList<String>();

      if (isLeaf()) {
          result.add(this.name);
          return result;
      }
      
      for (Node n : nodes) {
          result.addAll(n.getLeafNames());
      }
      return result;
  }
  
  
  public Node longestBranch() {
      if(isLeaf())
        return null;
        
      Node longest = null;
      double length = 0;
      for(Node n : nodes)
      {
          if(n.getBranchLength() > length)
            {
                longest = n;
                length = n.getBranchLength();
            }
      }
      return longest;
  }

  /**
   * Returns all of the nodes of the tree.
   */
  public ArrayList<Node> getNodes() {
      ArrayList<Node> result = new ArrayList<Node>();
      for (Node n: nodes) {
          result.addAll(n.getNodes());
      }
      result.add(this);
      return result;
  }

  /**
   * Add a child to the tree
   */
  public void addChild(Node child) {
    nodes.add(child);
    child.parent = this;
    child.setAnscestors(this.anscestors);
    child.getAnscestors().add(this);
    updateBranchColorFromChildren();
  }

  public void rotate() {
      Stack<Node> s = new Stack<Node>();
      for (Node n : nodes) {
          s.push(n);
      }
      nodes.clear();
      while (s.size() > 0) {
          nodes.add(s.pop());
      }
  }

  /**
   * Max depth of the tree (as a sum of branch lengths)
   */
  public double depthF() {
    double deepest = 0;
    for (int i = 0; i <nodes.size(); i++) {
      double depth = nodes.get(i).depth();
      if (depth > deepest) {
        deepest = depth;
      }
    }
    return deepest + getBranchLength();
  }


  /**
   * Returns the shortest root-to-tip branch length of the tree
   */
  public double shortestRootToTipDistance() {
    if (nodes.size() == 0) {
      return getBranchLength();
    }
    double shortest = 100000000;
    for (int i = 0; i < nodes.size(); i++) {
      double s = nodes.get(i).shortestRootToTipDistance();
      if (s < shortest) {
        shortest = s;
      }
    }
    return shortest + getBranchLength();
  }

  /**
   * Returns the longest root-to-tip branch length of the tree
   */
  public double longestRootToTipDistance() {
    if (nodes.size() == 0) {
      return getBranchLength();
    }
    double longest = -100000000;
    for (Node n : nodes) {
      double l = n.longestRootToTipDistance();
      if (l > longest) {
        longest = l;
      }
    }
    return longest + getBranchLength();
  }

  /**
   * Returns the longest label of any node in the tree
   */
  public String getLongestLabel() {
    String longest = getLabel();
    if (nodes.size() == 0) {
      return longest;
    }
    for(Node n : nodes) {
      String lbl = n.getLongestLabel();
      if (lbl.length() > longest.length()) {
        longest = lbl;
      }
    }
    return longest;
  }
  
  public String getLongestName() {
      String longest = getName();
      if (nodes.size() == 0) {
        return longest;
      }
      for(Node n : nodes) {
        String lbl = n.getLongestName();
        if (lbl.length() > longest.length()) {
          longest = lbl;
        }
      }
      return longest;
    }


  //SORTING METHODS
  public void sortByNumberOfOtus() {
    //sort each of the subtrees
    for (int i=0; i < nodes.size(); i++) {
      nodes.get(i).sortByNumberOfOtus();
    }

    //sort this node
    Collections.sort(nodes, new java.util.Comparator() {
      public int compare(Object o1, Object o2) {
        if ( ((Node)o1).getNumberOfLeaves() < ((Node)o2).getNumberOfLeaves()) {
          return -1;
        } else if ( ((Node)o1).getNumberOfLeaves() > ((Node)o2).getNumberOfLeaves()) {
          return 1;
        } else {
          return 0;
        }
      }
    });
  }
  
  public void sortByBranchLength() {
      for (int i=0; i < nodes.size(); i++) {
        nodes.get(i).sortByBranchLength();
      }

      //sort this node
      Collections.sort(nodes, new java.util.Comparator() {
        public int compare(Object o1, Object o2) {
          if ( ((Node)o1).longestRootToTipDistance() < ((Node)o2).longestRootToTipDistance()) {
            return -1;
          } else if ( ((Node)o1).longestRootToTipDistance() > ((Node)o2).longestRootToTipDistance()) {
            return 1;
          } else {
            return 0;
          }
        }
      });
  }

  public void sortByNumberOfChildren() {
        //sort each of the subtrees
    for (int i=0; i < nodes.size(); i++) {
      nodes.get(i).sortByNumberOfChildren();
    }

    //sort this node
    Collections.sort(nodes, new java.util.Comparator() {
      public int compare(Object o1, Object o2) {
        if ( ((Node)o1).nodes.size() < ((Node)o2).nodes.size()) {
          return -1;
        } else if ( ((Node)o1).nodes.size() > ((Node)o2).nodes.size()) {
          return 1;
        } else {
          return 0;
        }
      }
    });
  }

  /**
   * Set the color by blending the children's colors, weighted by the number of leaves in each child
   * this recursively works over the entire tree.
   */
  public void updateBranchColorFromChildren() {
    // if(isLeaf() && groupBranchColor.size() == 0) { 
    //     groupBranchColor.add(new Color(0,0,0));
    //     groupBranchWeight.add(getBranchLength());
    //     groupBranchValue.add("Uncolored");
    //     return;
    //     }
    if (isLeaf()) { aggregateBranchData(); return; }

    //make the lists empty
    branchColor = null;
    groupBranchColor = new ArrayList<Color>();
    groupBranchWeight = new ArrayList<Double>();
    groupBranchValue = new ArrayList<String>();
    for (int i=0; i < nodes.size(); i++) {
      //recursion
      nodes.get(i).updateBranchColorFromChildren();

      //get the overall color for this node
      for (int j = 0; j < nodes.get(i).groupBranchColor.size(); j++) {
        groupBranchColor.add(nodes.get(i).groupBranchColor.get(j));
        groupBranchWeight.add(nodes.get(i).groupBranchWeight.get(j)); 
        groupBranchValue.add(nodes.get(i).groupBranchValue.get(j));
      }
      
      // for(int k = 0; k < nodes.get(i).groupBranchValue.size(); k++)
      //     groupBranchValue.add(nodes.get(i).groupBranchValue.get(k));
        
    }
    aggregateBranchData();
  }
  
  public void updateLabelColorFromChildren() {
    if (isLeaf()) { aggregateLabelData(); return; }

    //make the lists empty
    labelColor = null;
    groupLabelColor = new ArrayList<Color>();
    groupLabelWeight = new ArrayList<Double>();
    groupLabelValue = new ArrayList<String>();
    
    for (int i=0; i < nodes.size(); i++) {
      //recursion
      nodes.get(i).updateLabelColorFromChildren();

      //get the overall color for this node
      for (int j = 0; j < nodes.get(i).groupLabelColor.size(); j++) {
        groupLabelColor.add(nodes.get(i).groupLabelColor.get(j));
        groupLabelWeight.add(nodes.get(i).groupLabelWeight.get(j));
        groupLabelValue.add(nodes.get(i).groupLabelValue.get(j));
      }
    }
    aggregateLabelData();
    // labelColored = true;
  }
  
  public double getLineWidthF() {
      if (isLeaf()) {
          return lineWidth; 
          }

      //make the lists empty
      double total = 0;
      for (int i=0; i < nodes.size(); i++) {
        //recursion
        nodes.get(i).updateLineWidthsFromChildren();
        total = total + nodes.get(i).getLineWidth();
      }
	// return max;
     return total/nodes.size();
    }
  
  public void updateLineWidthsFromChildren() {
    if (isLeaf()) { return; }

    //make the lists empty
    double total = 0;
    for (int i=0; i < nodes.size(); i++) {
      nodes.get(i).updateLineWidthsFromChildren();

      total = total + nodes.get(i).getLineWidth();
    }
    setLineWidth(total/nodes.size());
  }

  /**
   * Put all of the same colors together 
   */
  public void aggregateBranchData() {
    ArrayList<Color> newgroupBranchColor = new ArrayList<Color>();
    ArrayList<Double> newgroupBranchWeight = new ArrayList<Double>();
    ArrayList<String> newgroupBranchValue = new ArrayList<String>();

    for(int i = 0; i < groupBranchValue.size(); i++) {
        if(newgroupBranchValue.contains(groupBranchValue.get(i))) {
            int index = newgroupBranchValue.indexOf(groupBranchValue.get(i));
            newgroupBranchWeight.set(index, newgroupBranchWeight.get(index) + groupBranchWeight.get(i));
        } else {
        newgroupBranchValue.add(groupBranchValue.get(i));
        newgroupBranchColor.add(groupBranchColor.get(i));
        newgroupBranchWeight.add(groupBranchWeight.get(i));
      }
    }
    groupBranchWeight = newgroupBranchWeight;
    groupBranchColor = newgroupBranchColor;
    groupBranchValue = newgroupBranchValue;
  }
  
  public void aggregateLabelData() {
        ArrayList<Color> newgroupLabelColor = new ArrayList<Color>();
        ArrayList<Double> newgroupLabelWeight = new ArrayList<Double>();
        ArrayList<String> newgroupLabelValue = new ArrayList<String>();

        for(int i = 0; i < groupLabelValue.size(); i++) {
        if(newgroupLabelValue.contains(groupLabelValue.get(i))) {
            int index = newgroupLabelValue.indexOf(groupLabelValue.get(i));
            newgroupLabelWeight.set(index, newgroupLabelWeight.get(index) + groupLabelWeight.get(i));
        } else {
        newgroupLabelValue.add(groupLabelValue.get(i));
        newgroupLabelColor.add(groupLabelColor.get(i));
        newgroupLabelWeight.add(groupLabelWeight.get(i));
      }
    }
        groupLabelValue = newgroupLabelValue;
        groupLabelWeight = newgroupLabelWeight;
        groupLabelColor = newgroupLabelColor;
     }
}