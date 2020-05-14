/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "GraphMapping" is part of ahmed.library.
 * ahmed.library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ahmed.library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ahmedMLib.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 *
 * @author Ahmed Mazher
 */
//<editor-fold>
public class GraphMapping {

    private static final int maxCount = 999999999;
    private static final long linkIDMultiplier = 1000000000;
    private final HashMap<Long, Node> mapNodes = new HashMap<>();
    private final HashMap<Long, Link> mapLinks = new HashMap<>();
    private int nodeCount; // total number of node in the the the map
    private long searchIdCounter = 0; // give id for new search when searching
    //when a search finish it put its id in this set so new search can pick
    //one of them, if this set is empty increase the searchIdCounter and use it
    private HashSet<Long> unusedId = new HashSet<>();

    public GraphMapping() {
        nodeCount = 0;
    }

    public boolean addNode(long nodeID, Object value) {
        if (!containNode(nodeID)) {
            try {
                mapNodes.put(nodeID, new Node(nodeID, value));
            } catch (MaxNumberExceeded ex) {
                System.err.println(ex.getMessage());
            }
            nodeCount++;
            return true;
        }
        return false;
    }

    public boolean addNode(Node n) {
        //if the first condition evaluate to false the second will not excute
        if (!containNode(n.nodeID)) {
            mapNodes.put(n.nodeID, n);
            nodeCount++;
            return true;
        }
        return false;
    }

    private long calLinkID(long from, long to) {
        return to * linkIDMultiplier + from;
    }

    public boolean addLink(Node from, Node to, int cost) {
        if (containNode(from.nodeID) && containNode(to.nodeID) && !isLinked(from, to)) {
            Link l = new Link(from, to, cost);
            mapLinks.put(l.linkID, l);
            from.addLink(l);
            to.addLink(l);
            return true;
        }
        return false;
    }

    public boolean addLink(long fromId, long toId, int cost) {
        if (containNode(fromId) && containNode(toId) && !isLinked(fromId, toId)) {
            Node from = getNodeById(fromId);
            Node to = getNodeById(toId);
            Link l = new Link(from, to, cost);
            mapLinks.put(l.linkID, l);
            from.addLink(l);
            to.addLink(l);
            return true;
        }
        return false;
    }

    public boolean removeLink(Node from, Node to) {
        if (from != null && to != null && containNode(from.nodeID) && containNode(to.nodeID)) {
            Link link = getLink(from, to);
            if (link != null) {
                return mapLinks.remove(link.linkID, link)
                        && from.removeLink(link)
                        && to.removeLink(link);
            }
        }
        return false;
    }

    public boolean removeLink(long fromId, long toId) {
        return removeLink(getNodeById(fromId), getNodeById(toId));
    }

    public Node getNodeById(long id) {
        return mapNodes.get(id);
    }

    public boolean containNode(long id) {
        return mapNodes.containsKey(id);
    }

    public boolean isLinked(Node from, Node to) {
        return from != null && to != null && isLinked(from.nodeID, to.nodeID);
    }

    public boolean isLinked(long from, long to) {
        return containNode(from) && containNode(to)
                && mapLinks.containsKey(calLinkID(from, to));
    }

    public Link getLink(Node from, Node to) {
        if (from != null && to != null) {
            return getLink(from.nodeID, to.nodeID);
        }
        return null;
    }

    public Link getLink(long from, long to) {
        if (isLinked(from, to)) {
            return mapLinks.get(calLinkID(from, to));
        }
        return null;
    }

    public Collection<Link> getMapLinks() {
        return mapLinks.values();
    }

    public Collection<Node> getMapNodes() {
        return mapNodes.values();
    }
//############################################################################

    public class Node {

        private final long nodeID;
        private Object val;
        /**
         * the key is the key of the search algorithm that visits this node
         * value is object of class VisitedData which carry information about
         * the visit to this node by the search algorithm
         */
        private HashMap<Long, VisitedData> visited = new HashMap<>();
        private final HashMap<Long, Link> links = new HashMap<>();

        public Node(long id, Object value) throws MaxNumberExceeded {
            if (id > maxCount) {
                throw new MaxNumberExceeded();
            }
            nodeID = id;
            val = value;
        }

        private boolean addLink(Link link) {
            if (link != null && !links.containsKey(link.linkID)) {
                return links.putIfAbsent(link.linkID, link) == null;
            } else {
                return false;
            }
        }

        private boolean removeLink(Link link) {
            return links.remove(link.linkID, link);
        }

        public boolean equals(Node n) {
            return this.nodeID == n.nodeID;
        }
//        public Link getOutLinkTo(Node to){
//            return links.get(calLinkID(this.nodeID, to.nodeID));
//        }
//        public Link getInLinkFrom(Node from){
//            return links.get(calLinkID(from.nodeID, this.nodeID));
//        }

        public boolean isLinkedWith(Node n) {
            return links.containsKey(calLinkID(this.nodeID, n.nodeID))
                    || links.containsKey(calLinkID(n.nodeID, this.nodeID));
        }

        public Node getLinkedNode(Link link) {
            if (this.equals(link.from)) {
                return link.to;
            } else if (this.equals(link.to)) {
                return link.from;
            } else {
                return null;
            }
        }

        public long getNodeID() {
            return nodeID;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object o) {
            val = o;
        }

        public List<Link> getNodeOutLinks() {
            List<Link> outLinks = new ArrayList<>();
            for (Link l : links.values()) {
                if (this.equals(l.from)) {
                    outLinks.add(l);
                }
            }
            return outLinks.isEmpty() ? null : outLinks;
        }

        public List<Link> getNodeInLinks() {
            List<Link> inLinks = new ArrayList<>();
            for (Link l : links.values()) {
                if (this.equals(l.to)) {
                    inLinks.add(l);
                }
            }
            return inLinks.isEmpty() ? null : inLinks;
        }

        public List<Node> getChildNodes() {
            List<Node> nl = new ArrayList<>();
            for (Link l : links.values()) {
                if (this.equals(l.from)) {
                    nl.add(l.to);
                }
            }
            return nl.isEmpty() ? null : nl;
        }

        public void setVisited(long searchId, VisitedData d) {
            visited.put(searchId, d);
        }

        public VisitedData getVisited(long searchId) {
            return visited.get(searchId);
        }

        public boolean isVisited(long searchId) {
            return visited.containsKey(searchId) && visited.get(searchId).isVisited;
        }

        public boolean isEvaluated(long searchId) {
            return visited.containsKey(searchId) && visited.get(searchId).isEvaluated;
        }

    }
//#############################################################################

    public class Link {

        private final Node from, to;
        /**
         * the key is the key of the search algorithm that visits this node
         * value is object of class VisitedData which carry information about
         * the visit to this node by the search algorithm
         */
        private HashMap<Long, VisitedData> visited = new HashMap<>();
        //link id is used to facilitate link search by map
        //to make thing easier i used formula to calculate a id from
        //given node id lets say we have two nodes with a given id
        //12 , 21 we will multiply the smallest of them by the
        //adding both numbers
        private final long linkID;
        private int cost;

        private Link(Node from, Node to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
            linkID = calLinkID(from.nodeID, to.nodeID);
        }

        public boolean equals(Link l) {
            return from.equals(l.from) && to.equals(l.to);
        }

        public boolean isOutOf(Node n) {
            return from.equals(n);
        }

        public int getCost() {
            return this.cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public void setVisited(long searchId, VisitedData d) {
            visited.put(searchId, d);
        }

        public VisitedData getVisited(long searchId) {
            return visited.get(searchId);
        }

        public boolean isVisited(long searchId) {
            return visited.containsKey(searchId) && visited.get(searchId).isVisited;
        }

        public boolean isEvaluated(long searchId) {
            return visited.containsKey(searchId) && visited.get(searchId).isEvaluated;
        }

    }
//    public enum LinkN1N2Direction {
//    N1N2, N2N1 ;
//    public LinkN1N2Direction reverse() {
//        if (this == LinkN1N2Direction.N1N2) {
//            return LinkN1N2Direction.N2N1;
//        } else{
//            return LinkN1N2Direction.N1N2;
//        }
//    }

    public class Path {

        public List<Node> pathNodes = new ArrayList<>();

        public boolean addNode(Node n) {
            return pathNodes.add(n);
        }

        public boolean removeNode(long nodeID) {
            return pathNodes.remove(getNodeById(nodeID));
        }

        public Node endNode() {
            if (!pathNodes.isEmpty()) {
                return pathNodes.get(pathNodes.size() - 1);
            }
            return null;
        }

        public Node startNode() {
            if (!pathNodes.isEmpty()) {
                return pathNodes.get(0);
            }
            return null;
        }

        public Node previousNode(Node n) {
            int i = pathNodes.indexOf(n);
            if (i > 0) {
                return pathNodes.get(i - 1);
            }
            return null;
        }

        public boolean containNode(Node n) {
            return pathNodes.contains(n);
        }

    }

    //adapted and modified from anser by 200_success(moderator) at stackexchange
    //http://codereview.stackexchange.com/users/9357/200-success link to his
    //profile and answer link http://codereview.stackexchange.com/a/48530
    public class BreadthFirstIterator implements Iterator<Node> {

        private final PriorityQueue<Node> queue;
        private final long searchId;
        private LinkCostCalculator valCal;
        private SearchLimiter limiter;

        public BreadthFirstIterator(Node startNode, LinkCostCalculator c, SearchLimiter limiter) {
            this.limiter = limiter;
            valCal = c;
            searchIdCounter++;
            searchId = searchIdCounter;
            queue = new PriorityQueue<>(new Comparator<Node>() {
                //after a long depate i decided to cal cost by arranging node
                //that are in same distance level accoring to their cost value
                //putting the lower cost in the head of the queue so when
                //evaluated "isVisited" it will change the cost of neighboring
                //nodes of same level if the resulting cost of going to them are
                //lower
                @Override
                public int compare(Node n1, Node n2) {
                    if (n1.visited.get(searchId).dis == n2.visited.get(searchId).dis) {
                        if (n1.visited.get(searchId).pathCost == n2.visited.get(searchId).pathCost) {
                            return 0;
                        } else if (n1.visited.get(searchId).pathCost < n2.visited.get(searchId).pathCost) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else if (n1.visited.get(searchId).dis < n2.visited.get(searchId).dis) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            startNode.setVisited(searchId, new VisitedData(0, 0, false, true));
            this.queue.add(startNode);
        }

        private void pathCostCalulator(Node currentNode, Link targetLink, Node targetNode) {
            //path cost calulator which is cumulative value in the
            //previous node plus value of the link
            // in cyclic graph link may have two possible cost based on node conected to it
            // * is delegated the proplem to BFS search code where
            //it is solved by priorty queue and only visite the unvisted
            //node and now calculate the cost for target link and node
            double c1 = currentNode.getVisited(searchId).pathCost + targetLink.getCost();
            double c2 = targetNode.getVisited(searchId).pathCost;
            targetLink.getVisited(searchId).pathCost = c1;
            targetLink.getVisited(searchId).isVisited = true;
            if (c1 < c2) {
                targetNode.getVisited(searchId).pathCost = c1;
                queue.remove(targetNode);
                queue.add(targetNode);
            }
        }

        @Override
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override
        public Node next() {
            //removes from front of queue
            Node mother = queue.remove();
            //this line protect against circular graph
            if (!mother.isVisited(searchId)) {
                mother.getVisited(searchId).isVisited = true;
                for (Node child : mother.getChildNodes()) {
                    // protect against recycling in cyclic graph
                    Link l = getLink(mother, child);
                    // the limiter are object of interface path chooser
                    // to limit the DFS from searching the entire space of nodes
                    // and make it search selected one
                    if ((limiter == null || limiter.visit(mother, l, child))) {
                        if (!child.isEvaluated(searchId)) {
                            child.setVisited(searchId, new VisitedData());
                            child.getVisited(searchId).dis = mother.getVisited(searchId).dis + 1;
                            child.getVisited(searchId).isEvaluated = true;
                        }
                        if (!l.isEvaluated(searchId)) {
                            l.setVisited(searchId, new VisitedData());
                            l.getVisited(searchId).dis = mother.getVisited(searchId).dis + 1;
                            l.getVisited(searchId).isEvaluated = true;
                        }
                        if (valCal != null) {
                            l.setCost(valCal.calLinkCost(mother, l, child));
                        }
                        if (!child.isVisited(searchId)) {
                            this.queue.add(child);
                        }
                        pathCostCalulator(mother, l, child);
                    }
                }
                return mother;
            } else if (hasNext()) {
                return next();
            }
            return null;
        }

    }

    /**
     * this class iterate over all possible paths from start node to target node
     * using modifiable search criteria by passing implementation of PathChooser
     * and LinkCostCalculator interface
     *
     * this path finding algorithm start by using DFS from target node to start
     * node to calculate distance and link cumulative cost
     *
     * DFS can be limited to search selected nodes rather than searching the
     * entire graph by DFSlimiter parameter , the cost calculator is rather
     * complicated better leave the default and give links desired cost or use
     * with caution
     *
     * the path choosing is then done by going from start node to target node
     * evaluating distance and pathCost parameter using "choosers" parameter and
     * choosing a path accordingly
     *
     * note that the links out of start node to target node are sorted from
     * highest distance and cost to lower one
     */
    public class ShortestPathIterator implements Iterator<Path>, Iterable<Path> {

        private final Stack<Path> pathStack = new Stack<>();
        private Node currentNode;
        private final Node targetNode, startNode;
        private BreadthFirstIterator BFS;
        //private List<PathChooser> choosers = new LinkedList<>();
        //private long pathSearchId;
        boolean lowerCostFirst;

        public ShortestPathIterator(Node startNode, Node endNode,
                LinkCostCalculator cal, SearchLimiter limiter, boolean lowerCostFirst) {
            //PathChooser... choosers) {
            //searchIdCounter++ ;
            //pathSearchId = searchIdCounter ;
            this.lowerCostFirst = lowerCostFirst;
            BFS = new BreadthFirstIterator(startNode, cal, new SearchLimiter() {
                @Override
                public boolean visit(Node currentNode, Link targetLink, Node targetNode) {
                    return (limiter == null || limiter.visit(currentNode, targetLink, targetNode))
                            && !currentNode.equals(endNode);
                }
            });
//            if (choosers != null && choosers.length > 0) {
//                for (PathChooser p : choosers) {
//                    this.choosers.add(p);
//                }
//            } else {
//                this.choosers.add(new PathChooser() {
//                    @Override
//                    public boolean chooseLink(Node currentNode, Link targetLink, Node targetNode, long searchId) {
//                        return targetNode.getVisited(searchId).dis < currentNode.getVisited(searchId).dis;
//                    }
//                });
//            }
            while (BFS.hasNext()) {
                currentNode = BFS.next();
            }
            this.targetNode = endNode;
            this.startNode = startNode;
            Path p = new Path();
            p.addNode(this.targetNode);
            pathStack.push(p);
        }

        public Iterator newPathIterator(boolean lowerCostFirst) { //boolean useDefault,PathChooser... choosers) {
//            this.choosers.clear();
//            if (useDefault && choosers != null && choosers.length > 0) {
//                this.choosers.addAll(Arrays.asList(choosers));
//                this.choosers.add(new PathChooser() {
//                    @Override
//                    public boolean chooseLink(Node currentNode, Link targetLink, Node targetNode,long searchId) {
//                        return targetNode.getVisited(searchId).dis < currentNode.getVisited(searchId).dis;
//                    }
//                });
//            } else if (!useDefault && choosers != null && choosers.length > 0) {
//                this.choosers.addAll(Arrays.asList(choosers));
//            } else {
//                this.choosers.add(new PathChooser() {
//                    @Override
//                    public boolean chooseLink(Node currentNode, Link targetLink, Node targrtNode,long searchId) {
//                        return targrtNode.getVisited(searchId).dis < currentNode.getVisited(searchId).dis;
//                    }
//                });
//            }
            this.lowerCostFirst = lowerCostFirst;
            pathStack.clear();
            Path p = new Path();
            p.addNode(this.targetNode);
            pathStack.add(p);
            return this;
        }

        @Override
        public Iterator<Path> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return !pathStack.isEmpty();
        }

        @Override
        public Path next() {
            Path currentPath = null;
            while (!pathStack.isEmpty()) {
                //get the last pushed path in the stack
                currentPath = pathStack.pop();
                //get the last node in it and sort its links
                //so that the lowest dis or cost link appear at end of iteration
                //so it become at the top of the stack
                currentNode = currentPath.endNode();
                if (currentNode.equals(startNode)) {
                    break;
                }
                //the core of the algorithm without it the DFS will take
                //an arbitrary path that could be the longest one or circle
                List<Link> currentNodeLinks = currentNode.getNodeInLinks();
                Collections.sort(currentNodeLinks, Collections.reverseOrder(
                        new LinkComparator<Link>(BFS.searchId, lowerCostFirst)));
                //<editor-fold defaultstate="collapsed" desc="legacy comment">
//for every connected node to current node creat new path
                //add all nodes from the current path to newlly created
                //path and add this connected node to the end of the path
                //then add the path to the stack to expand it later note
                //that the path count are increasing exponentially as
                //depth increase and contine tell we find the target node
                //end while and return the path
//</editor-fold>
                //for (PathChooser c : choosers) {
                //boolean linkFound = false;
                for (Link l : currentNodeLinks) {
                    if (l.isVisited(BFS.searchId) //be sure that we go 
                            //through isVisited pass by BFS i.e not wander arbitrary
                            && currentNode.getLinkedNode(l) != null // not an closed path
                            //&& c.chooseLink(currentNode, l, currentNode.getLinkedNode(l), BFS.searchId)
                            //prevent going back in the same path nodes and entering in endless loop 
                            && !(currentPath.containNode(currentNode.getLinkedNode(l)))) {
                        Path temp = new Path();
                        temp.pathNodes.addAll(currentPath.pathNodes);
                        temp.addNode(currentNode.getLinkedNode(l));
                        pathStack.push(temp);
                        //linkFound = true;
                    }
                }
//                    if (linkFound) {
//                        break;
//                    }
                //}
            }
            return currentPath;
        }

    }

    private class MaxNumberExceeded extends Exception {

        @Override
        public String getMessage() {
            return super.getMessage() + "max number of allowed node and the \n"
                    + "number key  given to it can't exceed 999,999,999 \n"
                    + "for the purpose of link and node keying";
        }

    }

    /**
     * preventing the search algorithm from visiting all graph nodes and limit
     * the nodes & links to be visited by the visit function
     */
    public interface SearchLimiter {

        /**
         *
         * @param currentNode the current node during search
         * @param targetLink the target link out from current node to target
         * node
         * @param targetNode the target node passing through target link
         * @return the boolean value will be used to decide to search the target
         * node & link or not
         */
        public boolean visit(Node currentNode, Link targetLink, Node targetNode);

    }

    /**
     * will calculate cost for the following link if you don't know the cost of
     * link in advance you can use this interface method to give the link its
     * cost during search
     */
    public interface LinkCostCalculator {

        /**
         * will calculate cost for the following link if you don't know the cost
         * of link in advance you can use this interface method to give the link
         * its cost during search
         *
         * @param currentNode the current node during search
         * @param targetLink the target link out from current node to target
         * node
         * @param targetNode the target node passing through target link
         * @return
         */
        public int calLinkCost(Node currentNode, Link targetLink, Node targetNode);

    }

    //<editor-fold defaultstate="collapsed" desc="path chooser ">
//    /**
//     * used to decide which path to choose during path iteration
//     */
//    public interface PathChooser {
//        
//        /**
//         * used to decide which path to choose during path iteration it test
//         * available links of the last reached node in the path "current node"
//         * and decide which one to take if no link satisfied the condition no
//         * more advancement through this path is done .
//         *
//         * note that path choosing go backward from target node to start node
//         * so think in backward when you evaluate the path and take in account
//         * that going to start node meaning that the distance is usually decreasing
//         * and the cost also may be decreasing but surely they are the nodes
//         * that have been searched by BFS and not have been taken in the path
//         * before
//         *
//         * you can pass to
//         * ShortestPathIterator constructor or newPathSearch method of the
//         * ShortestPathIterator object multiple pathChooser object and the
//         * algorithm will test them --ONE AFTER ONE-- if no link satisfied the
//         * first pathCooser condition it will try the next one till a link found
//         * or no more pathChooser object available the following code are the
//         * default implementation of this interface
//         * <code>
//         * <pre>
//         * new PathChooser() {
//         *      &at Override
//         *          public boolean chooseLink(Node currentNode, Link targetLink, Node connectedNode,long searchId) {
//         *              // to go through the shortest path
//         *              return targetLink.getVisited(searchId).dis &lt currentNode.getVisited(searchId).dis;
//         *          }
//         *      })
//         *
//         * </pre>
//         * </code>
//         *
//         * @param currentNode the last node of the current evaluated path
//         * @param targetLink the current link of currentNode links being
//         * evaluated now
//         * @param targetNode the node to which the currentNode are connected by
//         * the targetLink
//         * @param searchId the value of searchId
//         *
//         * @return the boolean
//         */
//        public boolean chooseLink(Node currentNode, Link targetLink, Node targetNode, long searchId);
//    }
//</editor-fold>
    /**
     * class MSD "map search isVisited data "for carrying information about the
     * the performed search as the distance from the start and cumulative value
     * of path ! at least for now
     */
    public class VisitedData {

        /**
         * distance from start of the search ,marker for distance from start
         * node to help reconstruct the way back from target node
         */
        public double dis;
        /**
         * cumulative cost of path
         */
        public double pathCost;
        /**
         * if the node is isVisited or not i add this because i needed to add
         * search information to node before marking it as isVisited when it is
         * child of currently isVisited node previous to add this boolean i use
         * the presence of search id in in isVisited hashMap as indicator of
         * visiting limiting me to add information only to currently isVisited
         * node
         */
        public boolean isVisited;
        /**
         * has been given a value but not visited yet
         */
        public boolean isEvaluated;

        public VisitedData() {
            dis = Double.POSITIVE_INFINITY;
            pathCost = Double.POSITIVE_INFINITY;
            isVisited = false;
            isEvaluated = false;
        }

        public VisitedData(int dis, int pathCost, boolean isVisited, boolean isEvaluated) {
            this.dis = dis;
            this.pathCost = pathCost;
            this.isVisited = isVisited;
            this.isEvaluated = isEvaluated;
        }

    }

    public class LinkComparator<T extends Link> implements Comparator<T> {

        boolean orderByCostFirst;
        long searchId;

        public LinkComparator(long searchId, boolean orderByCostFirst) {
            this.searchId = searchId;
            this.orderByCostFirst = orderByCostFirst;
        }

        public LinkComparator(long searchId) {
            this.searchId = searchId;
            //to choose which is more important distance or cost
            this.orderByCostFirst = false;
            // when true ordering link by distance from highest to lowest distance
            // this will benefit the search algorithms to pick up the shortest
        }

        @Override
        public int compare(Link L1, Link L2) {
            //because i,m using stack in the path search
            //i want the path with the lowest cost appear
            //at end of iteration so it will be in the front
            //of the stack
            if (!L1.isVisited(searchId)) {
                return 1;
            }
            if (!L2.isVisited(searchId)) {
                return -1;
            }
            if (orderByCostFirst) {
                if (L1.visited.get(searchId).pathCost == L2.visited.get(searchId).pathCost) {
                    if (L1.visited.get(searchId).dis == L2.visited.get(searchId).dis) {
                        return 0;
                    } else if (L1.visited.get(searchId).dis < L2.visited.get(searchId).dis) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (L1.visited.get(searchId).pathCost < L2.visited.get(searchId).pathCost) {
                    return -1;
                } else {
                    return 1;
                }
            } else if (L1.getVisited(searchId).dis == L2.getVisited(searchId).dis) {
                if (L1.getVisited(searchId).pathCost == L2.getVisited(searchId).pathCost) {
                    return 0;
                } else if (L1.getVisited(searchId).pathCost < L2.getVisited(searchId).pathCost) {
                    return -1;
                } else {
                    return 1;
                }
            } else if (L1.getVisited(searchId).dis < L2.getVisited(searchId).dis) {
                return -1;
            } else {
                return 1;
            }
        }

    }

    public class MapEnteryValComparator<K, V> implements Comparator<Map.Entry<K, V>> {

        Comparator<V> myComparator;

        public MapEnteryValComparator(Comparator<V> myComparator) {
            this.myComparator = myComparator;
        }

        @Override
        public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
            return myComparator.compare(o1.getValue(), o2.getValue());
        }

    }

}
//</editor-fold>
