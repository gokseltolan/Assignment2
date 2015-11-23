package introsde.rest.ehealth.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="measureTypes")
@XmlAccessorType(XmlAccessType.FIELD)
public class MeasureType {

    @XmlElement
    private List<String> measureType = new ArrayList<String>();

    public MeasureType() {
    }

    public List<String> getMeasureType() {
        return measureType;
    }

    public void setMeasureType(List<String> measureType) {
        this.measureType = measureType;
    }
}
