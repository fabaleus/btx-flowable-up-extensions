/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.work;

import org.springframework.context.annotation.ComponentScan;

/**
 * A no-op class that functions as a package marker for a type-safe and
 * refactor-safe way of supplying Spring with a package name to scan for
 * components.
 * <p>
 * Recommended by Spring; see {@link ComponentScan#basePackageClasses}
 */
public class FlowableWorkPackageMarker {}
