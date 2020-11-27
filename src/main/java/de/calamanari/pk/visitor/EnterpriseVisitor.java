//@formatter:off
/*
 * Enterprise Visitor - interface to be implemented by all concrete VISITORs
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
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
package de.calamanari.pk.visitor;

/**
 * Enterprise Visitor - interface to be implemented by all concrete VISITORs
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface EnterpriseVisitor {

    /**
     * Visit a holding
     * 
     * @param holding element that can be visited
     */
    public void visit(CustomerHolding holding);

    /**
     * Visit a company
     * 
     * @param company element that can be visited
     */
    public void visit(CustomerCompany company);

    /**
     * Visit a customer company division
     * 
     * @param division element that can be visited
     */
    public void visit(CustomerDivision division);

    /**
     * Visit a customer order
     * 
     * @param order element that can be visited
     */
    public void visit(CustomerOrder order);

}
