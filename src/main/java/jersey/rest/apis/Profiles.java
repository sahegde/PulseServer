package jersey.rest.apis;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "profiles")
public class Profiles {
	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	List<Profile> profiles;
	
	public Profiles() {
		profiles = new ArrayList<Profile>();
	}
}
