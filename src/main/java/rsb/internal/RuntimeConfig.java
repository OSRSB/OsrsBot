package rsb.internal;

import java.util.Collections;
import java.util.Map;
import lombok.Data;

@Data
public class RuntimeConfig
{
    private Map<String, ?> props = Collections.emptyMap();
}
