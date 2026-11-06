package DataAccessLayer;

import BusinessLayer.Member;
import BusinessLayer.SubscriptionPeriod;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class MemberData {
    private static final File file = new File("MembersFile.txt");

    private static final DateTimeFormatter dateFormat =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static String joinMemberInfo(Member member, String delimiter) {
        String period = joinPeriodInfo(member.getSubscriptionPeriod(), "@@@@");
        return member.getMembershipKey() +
                delimiter + member.getName() +
                delimiter + member.getAddress() +
                delimiter + member.getContactInfo() +
                delimiter + member.getEmergencyContactInfo() +
                delimiter + period +
                delimiter + member.getBelt() +
                delimiter + member.getInstructorID();
    }

    private static String joinPeriodInfo(SubscriptionPeriod period, String delimiter) {
        return dateFormat.format(period.getStartDate())
                + delimiter
                + dateFormat.format(period.getEndDate())
                + delimiter
                + period.getPeriodType()
                +delimiter
                + period.getFees();
    }

    private static Member split(String memberInfo, String separator) {
        String[] infoToken = memberInfo.split(separator);
        return new Member(

                infoToken[0],
                infoToken[1],
                infoToken[2],
                infoToken[3],
                infoToken[4],
                splitPeriod(infoToken[5], "@@@@"),
                infoToken[6],
                Integer.parseInt(infoToken[7])
        );

    }
    private static SubscriptionPeriod splitPeriod(String periodInfo, String separator) {
        try {

            String[] periodTokens = periodInfo.split(separator);

            LocalDate startDate = LocalDate.parse(periodTokens[0],dateFormat);
            LocalDate endDate = LocalDate.parse(periodTokens[1],dateFormat);

            int periodType = Integer.parseInt(periodTokens[2]);
            double fees = Double.parseDouble(periodTokens[3]);

            return new SubscriptionPeriod(
                    startDate,
                    endDate,
                    periodType,
                    fees
            );

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }



    public static boolean saveNewMember(Member member) {
        // try with resources
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {

            writer.println(joinMemberInfo(member, "##//##"));
            return true;


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }



    }

    public static boolean resaveAllMember(ArrayList<Member> members) {

        try (PrintWriter writer = new PrintWriter(file)) {

            for (Member member : members) {
                if (!member.isMarkForDelete())
                    writer.println(joinMemberInfo(member, "##//##"));
            }

            return true;


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;

        }


    }

    public static ArrayList<Member> getAllMembers() {
        ArrayList<Member> members = new ArrayList<>();
        try (Scanner input = new Scanner(file)) {


            while (input.hasNextLine()) {
                String Line = input.nextLine();
                members.add(split(Line, "##//##"));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return members;
    }
}
