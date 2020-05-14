/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "DecisionNode" is part of ahmed.library.
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

/**
 *
 * @author Ahmed Mazher
 */
public class DecisionNode {

    private final long nodeId;
    private boolean isTerminationNode;
    private DecisionNode leftNode;
    private DecisionNode rightNode;
    private CheckableCondition condition;
    private ExcutableAction actionOnTrue;
    private ExcutableAction actionOnFalse;

    public DecisionNode(long nodeId, CheckableCondition condition, ExcutableAction actionOnTrue, ExcutableAction actionOnFalse) {
        if (condition == null || actionOnFalse == null || actionOnTrue == null) {
            throw new NullPointerException("you can't use null objects on constructor of this class");
        }
        this.nodeId = nodeId;
        this.condition = condition;
        this.actionOnTrue = actionOnTrue;
        this.actionOnFalse = actionOnFalse;
        isTerminationNode = false;
        leftNode = new DecisionNode();
        rightNode = new DecisionNode();
    }

    /**
     * package private constructor to create termination node with no condition
     * or action
     */
    DecisionNode() {
        this.nodeId = 0;
        this.isTerminationNode = true;
    }

    public long getNodeId() {
        return nodeId;
    }

    boolean isTerminationNode() {
        return isTerminationNode;
    }

    DecisionNode getLeftNode() {
        return leftNode;
    }

    DecisionNode getRightNode() {
        return rightNode;
    }

    void setLeftNode(DecisionNode leftNode) {
        if (leftNode != null) {
            this.leftNode = leftNode;
        }
    }

    void setRightNode(DecisionNode rightNode) {
        if (rightNode != null) {
            this.rightNode = rightNode;
        }
    }

    ExcutableAction getActionOnFalse() {
        return actionOnFalse;
    }

    ExcutableAction getActionOnTrue() {
        return actionOnTrue;
    }

    CheckableCondition getCondition() {
        return condition;
    }

}
