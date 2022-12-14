package com.equo.swt.internal.chromium.lib;

import org.eclipse.swt.internal.Callback;

///
/// Implement this structure to receive string values asynchronously.
///
public class cef_string_visitor_t {
  ///
  /// Base structure.
  ///
  public cef_base_ref_counted_t base;
  ///
  /// Method that will be executed.
  ///
  /** @field cast=(void*) */
  public long /* int */ visit;

  /** @field flags=no_gen */
  public long /* int */ ptr;
  /** @field flags=no_gen */
  public Callback visit_cb;
  /** @field flags=no_gen */
  public int refs;

  public static final int sizeof = ChromiumLib.cef_string_visitor_t_sizeof();
}