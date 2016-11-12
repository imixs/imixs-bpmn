package org.imixs.report.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
/**
 * 
 * 
 * http://www.vogella.com/tutorials/EclipseEditors/article.html
 * https://www.eclipse.org/articles/Article-Forms/article.html
 * 
 * @author rsoika
 *
 */
public class ReportEditorInput implements IEditorInput {

	 private final long id;

     public ReportEditorInput(long id) {
             this.id = id;
     }

     public long getId() {
             return id;
     }

     @Override
     public boolean exists() {
             return true;
     }

     @Override
     public ImageDescriptor getImageDescriptor() {
             return null;
     }

     @Override
     public String getName() {
             return String.valueOf(id);
     }

     @Override
     public IPersistableElement getPersistable() {
             return null;
     }

     @Override
     public String getToolTipText() {
             return "Imixs-Report Configuration";
     }

     @Override
     public <T> T getAdapter(Class<T> adapter) {
             return null;
     }

     @Override
     public int hashCode() {
             final int prime = 31;
             int result = 1;
             result = prime * result + (int) (id ^ (id >>> 32));
             return result;
     }

     @Override
     public boolean equals(Object obj) {
             if (this == obj)
                     return true;
             if (obj == null)
                     return false;
             if (getClass() != obj.getClass())
                     return false;
             ReportEditorInput other = (ReportEditorInput) obj;
             if (id != other.id)
                     return false;
             return true;
     }
}