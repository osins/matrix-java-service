package club.hm.matrix.auth.security.domain;

import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

@NoArgsConstructor
public class JtiSet extends HashSet<String> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public JtiSet(String... jtis) {
        super(Arrays.asList(jtis));
    }
}
