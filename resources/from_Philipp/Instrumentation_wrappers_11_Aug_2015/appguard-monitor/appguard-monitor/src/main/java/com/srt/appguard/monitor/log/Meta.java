package com.srt.appguard.monitor.log;

import android.os.Parcel;
import android.os.Parcelable;

public class Meta implements Parcelable {

	public enum MetaType {
		URI, METHOD, PHONE_NUMBER
	}

	public static final Parcelable.Creator<Meta> CREATOR = new Parcelable.Creator<Meta>() {
		@Override
		public Meta createFromParcel(Parcel source) {
			return new Meta(source);
		}

		@Override
		public Meta[] newArray(int size) {
			return new Meta[size];
		}
	};

	private final MetaType type;
	private final String value;

	public Meta(MetaType type, String value) {
		this.type = type;
		this.value = value;
	}

	public Meta(Parcel in) {
		type = (MetaType) in.readSerializable();
		value = in.readString();
	}
	
	public MetaType getType() {
	    return type;
    }
	
	public String getValue() {
	    return value;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeSerializable(type);
		out.writeString(value);
	}
	
	@Override
	public String toString() {
		return "[META: type = " + this.type + ", value = " + this.value + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Meta)) {
			return false;
		}
		
		Meta meta = (Meta) o;
		if (meta.type != this.type) {
			return false;
		}
		
		if (meta.value != null) {
			if (!meta.value.equals(this.value)) {
				return false;
			}
		} else {
			if (this.value != null) {
				return false;
			}
		}
		
		return true;
	}
}
