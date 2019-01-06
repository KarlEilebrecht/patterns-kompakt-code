//@formatter:off
/*
 * Abstract Enterprise Unit - the COMPOSITE implementing the component interface
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//@formatter:on
package de.calamanari.pk.composite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Abstract Enterprise Unit is the abstract base class of the COMPOSITE implementing the interface of the component (EnterpriseNode).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractEnterpriseUnit implements EnterpriseNode {

    /**
     * name of unit
     */
    private String name = null;

    /**
     * this unit's parent node
     */
    private AbstractEnterpriseUnit parentUnit = null;

    /**
     * nodes below this unit
     */
    private List<EnterpriseNode> childNodes = new ArrayList<>();

    /**
     * Creates unit of this name
     * 
     * @param name enterprise unit name
     */
    public AbstractEnterpriseUnit(String name) {
        this.name = name;
    }

    /**
     * Creates unit, name not set, yet
     */
    public AbstractEnterpriseUnit() {

    }

    /**
     * Sets the unit's name
     * 
     * @param name enterprise unit name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets this unit's parent unit
     * 
     * @param parentUnit the owning unit
     */
    public void setParent(AbstractEnterpriseUnit parentUnit) {
        this.parentUnit = parentUnit;
    }

    /**
     * Adds the given node to this unit's child nodes.
     * 
     * @param childNode NOT NULL
     */
    public void addChildNode(EnterpriseNode childNode) {
        if (!this.childNodes.contains(childNode) && childNode != null) {
            childNodes.add(childNode);
        }
    }

    /**
     * Removes the given child node from this unit's children.
     * 
     * @param childNode dependent node
     * @return true if the given node was one of this unit's child nodes and has been removed, otherwise false
     */
    public boolean remove(EnterpriseNode childNode) {
        return this.childNodes.remove(childNode);
    }

    /**
     * Returns the nodes below this node if any.
     * 
     * @return collection of nodes, NEVER null
     */
    public Collection<EnterpriseNode> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EnterpriseNode getParentNode() {
        return parentUnit;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + "('" + this.name + "')";
    }
}
