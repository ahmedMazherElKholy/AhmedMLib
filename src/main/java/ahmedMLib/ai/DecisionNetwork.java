/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "DecisionNetwork" is part of ahmed.library.
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
package ahmedMLib.ai;

import java.util.HashMap;

/**
 *
 * @author Ahmed Mazher
 */
public class DecisionNetwork {

    private final DecisionNode root;
    private final HashMap<Long, DecisionNode> networkNodes = new HashMap<>();

    public DecisionNetwork(DecisionNode root) {
        if (root == null) {
            throw new NullPointerException("you can't use null objects on constructor of this class");
        }
        this.root = root;
        try {
            addNode(new DecisionNode());//adding termination node that has id 0
            addNode(root);
        } catch (MapContainItemWithSameId ex) {
            System.err.println(ex.getMessage());
        }
    }

    public final void addNode(DecisionNode n) throws MapContainItemWithSameId {
        if (!networkNodes.containsKey(n.getNodeId())) {
            networkNodes.put(n.getNodeId(), n);
        } else {
            throw new MapContainItemWithSameId();
        }
    }

    /**
     *
     * @param linksScheme
     * @throws NodeWithSpecifiedIdNotFound
     * @throws PadSchemeFormat the node links scheme must be as follow comma
     * separated triplets each triplet are space separated numbers representing
     * head node right and left arm e.g 1 4 5,3 1 2,5 6 0 note that the number
     * zero are reserved to the termination node at which the network stops
     */
    public final void linkNetWorkNodes(String linksScheme) throws NodeWithSpecifiedIdNotFound, PadSchemeFormat {
        String[] triplets = linksScheme.split(",");
        for (String triplet : triplets) {
            String[] linkedNodesId = triplet.split(" ");
            if (linkedNodesId.length != 3) {
                throw new PadSchemeFormat();
            }
            DecisionNode[] linkedNodes = new DecisionNode[3];
            int count = 0;
            for (String n : linkedNodesId) {
                if (!networkNodes.containsKey(Long.valueOf(n))) {
                    throw new NodeWithSpecifiedIdNotFound(n);
                } else {
                    linkedNodes[count] = networkNodes.get(Long.valueOf(n));
                    count++;
                }
            }
            linkedNodes[0].setRightNode(linkedNodes[1]);
            linkedNodes[0].setLeftNode(linkedNodes[2]);
        }
    }

    public void analyze() {
        DecisionNode current = root;
        while (!current.isTerminationNode()) {
            if (current.getCondition().checkCondition()) {
                current.getActionOnTrue().excute();
                current = current.getRightNode();
            } else {
                current.getActionOnFalse().excute();
                current = current.getLeftNode();
            }
        }
    }

    @SuppressWarnings("serial")
    class MapContainItemWithSameId extends Exception {

        @Override
        public String getMessage() {
            return super.getMessage() + " you have added node with same id choose"
                    + " another one or remove it first";
        }

    }

    @SuppressWarnings("serial")
    class NodeWithSpecifiedIdNotFound extends Exception {

        String theNotFoundNodeId;

        public NodeWithSpecifiedIdNotFound(String theNotFoundNodeId) {
            super();
            this.theNotFoundNodeId = theNotFoundNodeId;
        }

        @Override
        public String getMessage() {
            return super.getMessage() + " the node with id number " + theNotFoundNodeId
                    + " not found in the network";
        }

    }

    @SuppressWarnings("serial")
    class PadSchemeFormat extends Exception {

        @Override
        public String getMessage() {
            return super.getMessage() + " the node links scheme must be as follow"
                    + " comma separated triplets each triplet are space separated"
                    + " numbers representing head node right and left arm"
                    + " e.g 1 4 5,3 1 2,5 6 0 note that the number zero are reserved"
                    + " to the termination node at which the network stops";
        }

    }

}
