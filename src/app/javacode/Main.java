package app.javacode;

public class Main {
    public static void main(String[] args) {
        MyStringBuilder stringBuilder = new MyStringBuilder();
        stringBuilder.append("Onsjd");
        stringBuilder.append("dlvos!");
        System.out.println(stringBuilder);

        stringBuilder.undo();
        System.out.println(stringBuilder);

        stringBuilder.append("twotwowtwo");
        stringBuilder.append("three");
        stringBuilder.append("focaodj");
        System.out.println(stringBuilder);

        stringBuilder.undo();
        System.out.println(stringBuilder);

        stringBuilder.undo();
        System.out.println(stringBuilder);

    }
}