package yeeaoo.frist2016_06_15;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yo on 2016/6/15.
 */
public class Person implements Parcelable{
    private String name;
    private int age;
    protected Person() {
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {//，注意这里读取的顺序一定要和刚才写出的顺序完全相同
            Person person = new Person();
            person.name = in.readString();
            person.age = in.readInt();
            return person;
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
