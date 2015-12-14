package com.srt.appguard.monitor.log;

import java.util.Arrays;

import com.srt.appguard.monitor.log.Meta.MetaType;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable, Comparable<Message> {

	public enum Type {
		INFO, WARNING, ERROR, DEBUG, ALLOW, DENY
	}

	public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
		@Override
		public Message createFromParcel(Parcel source) {
			return new Message(source);
		}

		@Override
		public Message[] newArray(int size) {
			return new Message[size];
		}
	};

	private static long ID = 0;

	private final long id;
	private final Type type;
	private final long date;
	private final String message;
	private Meta[] metas;

	public Message(Type type, String message, Meta... metas) {
		this.id = ID++;
		this.type = type;
		this.date = System.currentTimeMillis();
		this.message = message;
		this.metas = metas;
	}

	public Message(Parcel in) {
		this.id = in.readLong();
		this.type = Type.valueOf(in.readString());
		this.date = in.readLong();
		this.message = in.readString();
		this.metas = in.createTypedArray(Meta.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeString(type.toString());
		out.writeLong(date);
		out.writeString(message);
		out.writeTypedArray(metas, 0);
	}

	public Type getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "[MESSAGE: type = " + type + ", date = " + date + ", message = " + message + ", metas = " + (metas != null ? Arrays.asList(metas) : null) + "]";
	}

	public long getDate() {
		return date;
	}

	public Meta[] getMetas() {
		return metas;
	}

	public String getMetaValue(MetaType type) {
		for (Meta meta : metas) {
			if (meta.getType() == type) {
				return meta.getValue();
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Message)) {
			return false;
		}
		
		Message m = (Message) o;
		if (m.type != this.type) {
			return false;
		}
		
		if (m.message != null) {
			if (!m.message.equals(this.message)) {
				return false;
			}
		} else {
			if (this.message != null) {
				return false;
			}
		}
		
		// compare Metas (order-sensitive)
		if (m.metas != null) {
			if (this.metas == null || this.metas.length != m.metas.length) {
				return false;
			}
			
			for (int i = 0; i < m.metas.length; i++) {
				if (!m.metas[i].equals(this.metas[i])) {
					return false;
				}
			}
		} else {
			if (this.metas != null) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int compareTo(Message another) {
		if (another != null) {
			if (this.equals(another)) {
				return 0;
			} else if (date > another.date) {
				return 1;
			} else if (date < another.date) {
				return -1;
			} else if (id > another.id) {
				return 1;
			} else if (id < another.id) {
				return -1;
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}
}
