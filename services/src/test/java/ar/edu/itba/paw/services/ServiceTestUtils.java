package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;

import java.util.UUID;

public class ServiceTestUtils {
    static UUID ITBA_ID = UUID.fromString("10000000-0000-0000-0000-000000000000");
    static UUID ING_INF = UUID.fromString("c0000000-0000-0000-0000-000000000000");
    static UUID ING_MEC = UUID.fromString("c0000000-0000-0000-0000-000000000001");
    static UUID EDA_ID = UUID.fromString("50000000-0000-0000-0000-000000000000");
    static UUID EDA_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000000");
    static UUID PAW_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000001");
    static UUID GUIAS_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000005");
    static UUID THEORY_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000007");
    static UUID MVC_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000008");
    static UUID BASURA_ID = UUID.fromString("d0000000-0000-0000-0000-000000000009");
    static UUID MVC_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000002");
    static UUID GUIA1EDA_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000000");
    static UUID PARCIAL_DINAMICA_FLUIDOS_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000004");
    static UUID PEPE_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static UUID SAIDMAN_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");
    static UUID ADMIN_ID = UUID.fromString("00000000-0000-0000-0000-00000000000A");

    static UUID MATE_ID = UUID.fromString("50000000-0000-0000-0000-000000000005");
    static UUID TVM_ID = UUID.fromString("a0000000-0000-0000-0000-000000000006");


    //inserted in test
    static UUID TMP_PARENT_DIR_ID = UUID.fromString("dF000000-0000-0000-0000-000000000000");
    static UUID TMP_NOTE_ID_1 = UUID.fromString("aF000000-0000-0000-0000-000000000001");
    static UUID TMP_NOTE_ID_2 = UUID.fromString("aF000000-0000-0000-0000-000000000002");
    static UUID TMP_NOTE_ID_3 = UUID.fromString("aF000000-0000-0000-0000-000000000003");
    static UUID TMP_NOTE_ID_4 = UUID.fromString("aF000000-0000-0000-0000-000000000004");

    static UUID TMP_DIR_ID_1 = UUID.fromString("dF000000-0000-0000-0000-000000000001");
    static UUID TMP_DIR_ID_2 = UUID.fromString("dF000000-0000-0000-0000-000000000002");
    static UUID TMP_DIR_ID_3 = UUID.fromString("dF000000-0000-0000-0000-000000000003");
    static UUID TMP_DIR_ID_4 = UUID.fromString("dF000000-0000-0000-0000-000000000004");
    private ServiceTestUtils() {}

    static User mockUser() {
        return new User(PEPE_ID,
                "John",
                "Doe",
                "JohnDoe",
                "pepe@itba.edu.ar",
                null,
                UserStatus.ACTIVE,
                new String[]{Role.ROLE_STUDENT.name()},
                "es",
                null,
                null
        );
    }

    static User mockAdmin() {
        return new User(ADMIN_ID,
                "super",
                "admin",
                "superadmin",
                "admin@apuntea.edu.ar",
                null,
                UserStatus.ACTIVE,
                new String[]{Role.ROLE_ADMIN.name()},
                "en",
                null,
                null
       );
    }

    static Directory mockDirectory(String name) {
        return new Directory(UUID.randomUUID(),
                name,
                null,
                EDA_DIRECTORY_ID,
                null,
                null,
                true,
                null
        );
    }
}
