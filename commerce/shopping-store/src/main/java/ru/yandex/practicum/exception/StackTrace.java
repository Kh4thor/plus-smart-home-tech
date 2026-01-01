package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class StackTrace {
    private String classLoaderName;
    private String moduleName;
    private String moduleVersion;
    private String methodName;
    private String fileName;
    private Integer lineNumber;
    private String className;
    private boolean nativeMethod;
}
